package com.horseloft.jpa.service;

import com.alibaba.fastjson.JSONObject;
import com.horseloft.jpa.constant.MessageConstant;
import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.constant.UserConstant;
import com.horseloft.jpa.db.dao.*;
import com.horseloft.jpa.db.entity.*;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import com.horseloft.jpa.utils.*;
import com.horseloft.jpa.vo.*;
import com.horseloft.jpa.vo.structure.UserStructureListResponseVo;
import com.horseloft.jpa.vo.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Date: 2020/1/4 16:20
 * User: YHC
 * Desc: 用户服务
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PublicService publicService;

    /**
     * 用户登录
     * @param params
     * @return
     */
    public ResponseVo<UserLoginResponseVo> userLogin(UserLoginRequestVo params) {
        String password = TokenUtils.tokenDecode(params.getPassword(), UserConstant.PASSWORD_AES_KEY);
        if (!ParamsUtils.isUserAccount(params.getAccount()) || !ParamsUtils.isUserPassword(password)) {
            throw BusinessException.ofErrorParameter("账号或密码错误，请重新输入");
        }

        User user = userDao.getActiveInfoByAccount(params.getAccount());
        if (user == null || !StringUtils.equals(user.getPassword(), ParamsUtils.makeMD5Password(password))) {
            throw BusinessException.ofErrorParameter("账号或密码错误，请重新输入");
        }

        //只有工人角色、不能登录非工人入口|工人登录入口必须有工人角色
        if ((user.getRoleId().equals(RoleConstant.WORKER_ROLE_ID_STR) && !params.isWorkerLogin()) ||
                (params.isWorkerLogin() && !user.getRoleId().contains(RoleConstant.WORKER_ROLE_ID_STR))) {
            return ResponseVo.ofError(ResponseCode.UNAUTHORIZED);
        }

        StringBuilder structureName = new StringBuilder();
        //工厂
        if (user.getCompanyId() > 0) {
            Company company = companyDao.findCompanyByIdAndDeleteStatus(user.getCompanyId(), false);
            if (company != null) {
                structureName.append(company.getComName());
            }
        }
        // 公司
        if (user.getFactoryId() > 0) {
            Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(user.getFactoryId(), false);
            if (factory != null) {
                structureName.append(factory.getFactoryName());
            }
        }
        // 车间
        if (user.getWorkshopId() > 0) {
            Workshop workshop = workshopDao.findWorkshopByIdAndDeleteStatus(user.getWorkshopId(), false);
            if (workshop != null) {
                structureName.append(workshop.getWorkshopName());
            }
        }

        // 登录过期时间
        long expireTime;
        int autoLogin;
        if (params.getAutoLoginStatus()) {
            expireTime = DateUtils.getMillisTimeAfterDay(7);
            autoLogin = 1;
        } else {
            expireTime = DateUtils.getMillisTimeAfterHour(24);
            autoLogin = 0;
        }

        //设置token userId设置为20位长度字符串 不足则补0
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", TokenUtils.userIdAppend(user.getId()));
        String key = TokenUtils.encodeKey();
        String token = TokenUtils.tokenEncode(jsonObject.toJSONString(), key);

        //key 和 token 登录过期时间 写入数据库
        int row = userDao.updateUserToken(key, token, user.getId(), expireTime, autoLogin);
        if (row == 0) {
            throw BusinessException.ofErrorParameter("服务异常，请稍后");
        }

        UserLoginResponseVo responseVo = new UserLoginResponseVo();
        responseVo.setPasswordStatus(user.getPasswordStatus() ? 1 : 0);
        responseVo.setRealName(user.getRealName());
        responseVo.setStructure(structureName.toString());
        responseVo.setToken(token);

        return ResponseVo.ofSuccess(responseVo);
    }

    /**
     * 退出登录
     * @param params
     * @return
     */
    public ResponseVo<String> userLogout(RequestVo params){
        if (params.getUserId() == null) {
            return ResponseVo.ofError("用户未登录");
        }
        int affectRow = userDao.updateOnUserLogout(params.getUserId());
        if (affectRow > 0) {
            log.info("退出登录成功 {} ", params.getUserId());
        } else {
            log.info("退出登录失败 {} ", params.getUserId());
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 密码修改|数字或字母或组合 长度大于等于6
     * @param params
     * @return
     */
    public ResponseVo<String> userPasswordModify(PasswordEditRequestVo params) {
        String password = TokenUtils.tokenDecode(params.getPassword(), UserConstant.PASSWORD_AES_KEY);
        if (!ParamsUtils.isUserPassword(password)) {
            return ResponseVo.ofError("密码格式错误");
        }
        String passwordMD5 = ParamsUtils.makeMD5Password(password);
        if (passwordMD5 == null) {
            return ResponseVo.ofError("修改密码失败，请重试");
        }
        userDao.updatePasswordById(params.getUserId(), passwordMD5);

        return ResponseVo.ofSuccess();
    }

    /**
     * 当前用户组织结构
     * @param params
     * @return
     */
    public ResponseVo<List<UserStructureListResponseVo>> userStructureList(RequestVo params) {
        User user = userDao.getActiveUserById(params.getUserId());
        if (user == null) {
            return ResponseVo.ofError(MessageConstant.USER_STATE_ERROR);
        }
        //管理员查看全部公司|车间员工 仅有车间权限|工厂员工 有当前工厂及工厂下的车间的权限|公司员工 有当前公司及公司下的工厂及工厂下的车间的权限
        List<Factory> factoryList = new ArrayList<>();
        List<Workshop> workshopList = new ArrayList<>();
        List<Company> companyList = new ArrayList<>();
        if (params.getUser().getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            //管理员
            companyList = companyDao.getAllCompany();
            factoryList = factoryDao.getAllFactory();
            workshopList = workshopDao.getAllWorkshop();
        } else {
            Company company = companyDao.findCompanyByIdAndDeleteStatus(user.getCompanyId(), false);
            if (company == null) {
                return ResponseVo.ofError("您的所属公司信息异常");
            }
            companyList.add(company);
            if (user.getWorkshopId() > 0) {
                //车间员工
                Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(user.getFactoryId(), false);
                if (factory == null) {
                    return ResponseVo.ofError("您的所属工厂信息异常");
                }
                Workshop workshop = workshopDao.findWorkshopByIdAndDeleteStatus(user.getWorkshopId(), false);
                if (workshop == null) {
                    return ResponseVo.ofError("您的所属车间信息异常");
                }
                factoryList.add(factory);
                workshopList.add(workshop);
            } else if (user.getFactoryId() > 0) {
                //工厂员工
                Factory factory = factoryDao.findFactoryByIdAndDeleteStatus(user.getFactoryId(), false);
                if (factory == null) {
                    return ResponseVo.ofError("您的所属工厂信息异常");
                }
                factoryList.add(factory);
                workshopList = workshopDao.getAllFactoryWorkshop(user.getFactoryId());
            } else {
                //公司员工
                factoryList = factoryDao.getAllCompanyFactory(user.getCompanyId());
                workshopList = workshopDao.getAllCompanyWorkshop(user.getCompanyId());
            }
        }
        //必须有完整的三级数据结构
        if (companyList.isEmpty() || factoryList.isEmpty() || workshopList.isEmpty()) {
            return ResponseVo.ofError("数据信息异常，请刷新页面重试");
        }

        List<UserStructureListResponseVo> userCompanyList = new ArrayList<>();
        for(Company company : companyList) {

            List<UserStructureListResponseVo> userFactoryList = new ArrayList<>();
            for(Factory factory : factoryList) {
                if (factory.getCompanyId().equals(company.getId())) {

                    List<UserStructureListResponseVo> userWorkshopList = new ArrayList<>();
                    for(Workshop workshop : workshopList) {
                        if (workshop.getFactoryId().equals(factory.getId())) {
                            UserStructureListResponseVo userWorkshop = new UserStructureListResponseVo();
                            userWorkshop.setId(workshop.getId());
                            userWorkshop.setName(workshop.getWorkshopName());
                            userWorkshop.setList(new ArrayList<>());
                            userWorkshopList.add(userWorkshop);
                        }
                    }
                    UserStructureListResponseVo userFactory = new UserStructureListResponseVo();
                    userFactory.setId(factory.getId());
                    userFactory.setName(factory.getFactoryName());
                    userFactory.setList(userWorkshopList);
                    userFactoryList.add(userFactory);
                }
            }
            UserStructureListResponseVo userCompany = new UserStructureListResponseVo();
            userCompany.setId(company.getId());
            userCompany.setName(company.getComName());
            userCompany.setList(userFactoryList);
            userCompanyList.add(userCompany);
        }

        return ResponseVo.ofSuccess(userCompanyList);
    }

    /**
     * 新增用户|返回新增用户的id
     * @param params
     * @return
     */
    public ResponseVo<ResponseIdVo> userInfoAdd(UserAddRequestVo params) {
        //账号不能重复|新增的账号生成默认密码 123456
        User user;
        if (params.getId() > 0) {
            //当前用户信息
            user = userDao.getActiveUserById(params.getId());
            if (user == null) {
                return ResponseVo.ofError("当前账号信息异常");
            }
            if (!user.getAccount().equals(params.getAccount()) && userDao.findByAccountAndDeleteStatus(params.getAccount(), false) != null) {
                return ResponseVo.ofError("当前账号已存在");
            }
            //编辑状态密码修改
            if (StringUtils.isBlank(params.getPassword())) {
                params.setPassword(user.getPassword());
            } else {
                //对称解密
                String password = TokenUtils.tokenDecode(params.getPassword(), UserConstant.PASSWORD_AES_KEY);
                if (!ParamsUtils.isUserPassword(password)) {
                    return ResponseVo.ofError("密码格式错误");
                }
                params.setPassword(ParamsUtils.makeMD5Password(password));
            }
        } else {
            user = userDao.findByAccountAndDeleteStatus(params.getAccount(), false);
            if (user != null) {
                return ResponseVo.ofError("当前账号已存在");
            }
            user = new User();
            //生成默认密码
            params.setPassword(ParamsUtils.makeMD5Password(UserConstant.USER_DEFAULT_PASSWORD));
        }
        // 角色
        List<Long> roleIds = ConvertUtils.strToListLong(params.getRoleId(), ",");
        if (roleIds.isEmpty()) {
            return ResponseVo.ofError("角色参数格式错误");
        }
        List<Role> roleList = roleDao.findRolesByIdInAndDeleteStatus(roleIds, false);
        if (roleList.isEmpty() || roleList.size() != roleIds.size()) {
            return ResponseVo.ofError("您选择的角色不存在，请刷新页面重试");
        }

        // 手机号
        if (StringUtils.isNotEmpty(params.getMobile()) && !ParamsUtils.isMobile(params.getMobile())) {
            return ResponseVo.ofError("手机号格式错误");
        }
        user.setHouseholdRegister(params.isHouseholdRegister());
        user.setAccountStatus(params.isAccountStatus());
        user.setBankAccount(params.getBankAccount());
        user.setWorkshopId(params.getWorkshopId());
        user.setFactoryId(params.getFactoryId());
        user.setCompanyId(params.getCompanyId());
        user.setJobStatus(params.isJobStatus());
        user.setEntryDate(params.getEntryDate());
        user.setPassword(params.getPassword());
        user.setBirthday(params.getBirthday());
        user.setIdNumber(params.getIdNumber());
        user.setRealName(params.getRealName());
        user.setAddress(params.getAddress());
        user.setAccount(params.getAccount());
        user.setMobile(params.getMobile());
        user.setRemark(params.getRemark());
        user.setRoleId(ConvertUtils.strNumberAppendComma(params.getRoleId()));
        user.setSex(params.isSex());
        userDao.save(user);

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(user.getId());

        return ResponseVo.ofSuccess(responseIdVo);
    }

    /**
     * 用户详情
     * @param params
     * @return
     */
    public ResponseVo<UserAddRequestVo> getUserDetailById(RequestIdVo params) {
        UserAddRequestVo vo = new UserAddRequestVo();
        if (params.getId().equals(UserConstant.ADMIN_USER_ID)) {
            return ResponseVo.ofSuccess(vo);
        }
        User user = userDao.findUserByIdAndDeleteStatus(params.getId(), false);
        if (user == null) {
            return ResponseVo.ofSuccess(vo);
        }
        BeanUtils.copyProperties(user, vo);
        vo.setPassword("");

        return ResponseVo.ofSuccess(vo);
    }

    /**
     * 删除用户|只能删除离职或无效的用户
     * @param params
     * @return
     */
    public ResponseVo<String> userDeleteByIds(RequestIdVo params) {
        if (params.getId().equals(UserConstant.ADMIN_USER_ID)) {
            return ResponseVo.ofSuccess();
        }

        User user = userDao.findUserByIdAndDeleteStatus(params.getId(), false);
        if (user != null) {
            if (user.getAccountStatus()) {
                return ResponseVo.ofError("不能删除有效的账号");
            }
            if (user.getJobStatus()) {
                return ResponseVo.ofError("不能删除在职的账号");
            }
            user.setAccount(StringUtils.appendRandom(user.getAccount()));
            user.setDeleteStatus(true);
            userDao.save(user);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 修改在职状态
     * @param params
     * @return
     */
    public ResponseVo<String> userJobStatusChangeById(RequestIdVo params) {
        User user = userDao.findUserByIdAndDeleteStatus(params.getId(), false);
        if (user != null && !user.getId().equals(UserConstant.ADMIN_USER_ID)) {
            user.setJobStatus(!user.getJobStatus());
            userDao.save(user);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 修改账号有效状态
     * @param params
     * @return
     */
    public ResponseVo<String> userAccountStatusChangeById(RequestIdVo params) {
        User user = userDao.findUserByIdAndDeleteStatus(params.getId(), false);
        if (user != null && !user.getId().equals(UserConstant.ADMIN_USER_ID)) {
            user.setAccountStatus(!user.getAccountStatus());
            userDao.save(user);
        }
        return ResponseVo.ofSuccess();
    }

    /**
     * 用户列表数据下载
     * @param params
     */
    public void userListDataDownload(UserListRequestVo params, HttpServletResponse response) throws IOException {
        Sort sort = Sort.by(Sort.Order.asc("id"));
        List<User> userList = userDao.findAll(this.userListSpecification(params), sort);
        if (userList.isEmpty()) {
            throw new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), "您下载的数据为空");
        }
        List<UserListResponseVo> list = this.userListMixer(userList, true);
        //下载
        FileUtils.excelDownload(response, list, "用户列表");
    }

    /**
     * 用户列表|区分当前登录人的组织结构
     * @param params
     * @return
     */
    public ResponseVo<ResponseListVo<UserListResponseVo>> userListByCurrentUser(UserListRequestVo params) {
        Sort sort = Sort.by(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getPageSize(), sort);
        Page<User> userPage = userDao.findAll(this.userListSpecification(params), pageable);

        //响应
        ResponseListVo<UserListResponseVo> responseListVo = new ResponseListVo<>();
        responseListVo.setTotalPage(userPage.getTotalPages());
        responseListVo.setTotal(userPage.getTotalElements());
        responseListVo.setPageSize(params.getPageSize());
        responseListVo.setPage(params.getPage());

        List<User> userList = userPage.getContent();
        if (userList.isEmpty()) {
            return ResponseVo.ofSuccess(responseListVo);
        }
        responseListVo.setList(this.userListMixer(userList, false));

        return ResponseVo.ofSuccess(responseListVo);
    }

    //用户列表查询条件
    private Specification<User> userListSpecification(UserListRequestVo params) {
        return (Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            //超级管理员不展示
            list.add(criteriaBuilder.gt(root.get("id").as(Long.class), UserConstant.ADMIN_USER_ID));
            //删除的用户不展示
            list.add(criteriaBuilder.equal(root.get("deleteStatus").as(Integer.class), 0));

            // 账号
            if (NumberUtils.isActiveLong(params.getRoleId())) {
                list.add(criteriaBuilder.equal(root.get("roleId").as(Long.class), params.getRoleId()));
            }
            //账号状态
            if (params.getAccountStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("accountStatus").as(Boolean.class), params.getAccountStatus()));
            }
            //在职状态
            if (params.getJobStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("jobStatus").as(Boolean.class), params.getJobStatus()));
            }

            //组织架构
            PublicService publicService = new PublicService();
            Map<String, Object> map = publicService.getSearchStructure(params.getUser(), params.getCompanyId(), params.getFactoryId(), params.getWorkshopId());
            if (!map.isEmpty()) {
                list.add(criteriaBuilder.equal(root.get(map.get("name").toString()).as(Long.class), map.get("value")));
            }

            //入职时间
            if (params.getEntryDateStart() != null) {
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("entryDate").as(Date.class), params.getEntryDateStart()));
            }
            if (params.getEntryDateEnd() != null) {
                list.add(criteriaBuilder.lessThanOrEqualTo(root.get("entryDate").as(Date.class), params.getEntryDateEnd()));
            }

            Predicate[] predicates = new Predicate[list.size()];
            Predicate predicateAnd = criteriaBuilder.and(list.toArray(predicates));

            //or条件
            List<Predicate> listOr = new ArrayList<>();
            //姓名、账号
            if (StringUtils.isNotBlank(params.getSearchValue())) {
                listOr.add(criteriaBuilder.like(root.get("account").as(String.class), "%" + params.getSearchValue() + "%"));
                listOr.add(criteriaBuilder.like(root.get("realName").as(String.class), "%" + params.getSearchValue() + "%"));
            }

            //存在or条件为空的情况
            if (listOr.isEmpty()) {
                return predicateAnd;
            } else {
                Predicate[] predicatesOr = new Predicate[listOr.size()];
                Predicate predicateOr = criteriaBuilder.or(listOr.toArray(predicatesOr));
                return query.where(predicateAnd, predicateOr).getRestriction();
            }
        };
    }

    //用户列表数据处理|上一步判断空userList
    private List<UserListResponseVo> userListMixer(List<User> userList, Boolean isDownload) {
        // 角色名 组织架构
        Set<Long> roleIds = new HashSet<>();
        userList.forEach(x -> {
            List<Long> roleIdList = ConvertUtils.strToListLong(x.getRoleId(), ",");
            if (!roleIdList.isEmpty()) {
                roleIds.addAll(roleIdList);
            }
        });
        List<Role> roleList = new ArrayList<>();
        if (!roleIds.isEmpty()) {
            roleList = roleDao.findRolesByIdIn(new ArrayList<>(roleIds));
        }

        //组织架构
        Map<String, List<?>> listMap = publicService.getStructureDataByList(userList);

        //数据整合
        List<UserListResponseVo> userListResponseVoList = new ArrayList<>();
        for(User user : userList) {
            UserListResponseVo userListResponseVo = new UserListResponseVo();
            BeanUtils.copyProperties(user, userListResponseVo);

            //角色名，多个角色
            StringBuilder roleString = new StringBuilder();
            for(Role role : roleList) {
                if (user.getRoleId().contains(role.getId() + ",")) {
                    roleString.append(role.getRoleName()).append(",");
                }
            }
            userListResponseVo.setRoleName(roleString.toString().substring(0, roleString.length() - 1));

            //组织架构|格式：公司名-工厂名-车间名
            String structureName = publicService.getStructureName(listMap, user.getCompanyId(),user.getFactoryId(), user.getWorkshopId());
            userListResponseVo.setStructure(structureName);

            if (isDownload) {
                if (user.getJobStatus()) {
                    userListResponseVo.setJobStatusText("在职");
                } else {
                    userListResponseVo.setJobStatusText("离职");
                }
                if (user.getAccountStatus()) {
                    userListResponseVo.setAccountStatusTest("有效");
                } else {
                    userListResponseVo.setAccountStatusTest("无效");
                }
            }
            userListResponseVoList.add(userListResponseVo);
        }
        return userListResponseVoList;
    }

}
