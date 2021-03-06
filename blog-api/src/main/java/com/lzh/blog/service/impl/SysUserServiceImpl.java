package com.lzh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lzh.blog.dao.mapper.SysUserMapper;
import com.lzh.blog.dao.pojo.Article;
import com.lzh.blog.dao.pojo.SysUser;
import com.lzh.blog.service.LoginService;
import com.lzh.blog.service.SysUserService;
import com.lzh.blog.vo.ErrorCode;
import com.lzh.blog.vo.LoginUserVo;
import com.lzh.blog.vo.Result;
import com.lzh.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("佚名");
        }//数据有可能为空，加一个空指针判断
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1  token合法性校验  是否为空 解析是否为空 redis中是否存在
         * 2  如果校验失败，返回错误；
         * 3  如果成功返回对应结果  loginuservo
         */

        SysUser sysUser = loginService.checkToken(token);

        if(sysUser==null){
            return Result.fail(ErrorCode.TOKEBN_ERROR.getCode(), ErrorCode.TOKEBN_ERROR.getMsg());
        }//感觉这段代码多余了

        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        /**
         * 保存用户id会自动生成
         * 这个地方生成的id是分布式id 雪花算法
         */
        sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long authorId) {
        UserVo userVo = new UserVo();
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if(sysUser == null){
             sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("佚名");
        }
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
