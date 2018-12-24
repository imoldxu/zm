package com.zm.service.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.service.entity.Account;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.mapper.AccountMapper;
import com.zm.service.utils.RedissonUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class AccountService {

	@Autowired
	RedissonUtil redissonUtil;
	@Autowired
	AccountMapper accountMapper;

	public Account getMyAccount(Integer uid) {
		Example example = new Example(Account.class); 
		example.createCriteria().andEqualTo("uid", uid);
		Account a = accountMapper.selectOneByExample(example);
		if(a==null){
			a = new Account();
			a.setCash(0);
			a.setCoin(0);
			a.setLockedcash(0);
			a.setUid(uid);
			accountMapper.insertUseGeneratedKeys(a);
		}
		return a;
	}

	@Transactional
	public void withdrawCash(Integer uid, int amount) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		if(account.getCash()>=amount){
			account.setCash(account.getCash()-amount);
			accountMapper.updateByPrimaryKey(account);
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			//TODO：记录账户记录
		}else{
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			throw new HandleException(ErrorCode.NORMAL_ERROR, "余额不足");
		}
		return;
	}

	@Transactional
	public Account addCoin(Integer uid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		account.setCoin(account.getCoin()+amount);
		accountMapper.updateByPrimaryKey(account);
		
		//TODO：记录账户记录
		
		redissonUtil.unlock("USER_ACCOUNT_"+uid);
		return account;
	}
	
	@Transactional
	public Account addCash(Integer uid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		account.setCash(account.getCash()+amount);
		accountMapper.updateByPrimaryKey(account);
		
		//TODO：记录账户记录
		
		redissonUtil.unlock("USER_ACCOUNT_"+uid);
		return account;
	}

	@Transactional
	public Account reduceCoin(Integer uid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		if(account.getCoin()>=amount){
			account.setCoin(account.getCoin()-amount);
			accountMapper.updateByPrimaryKey(account);
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			//TODO：记录账户记录
		}else{
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			throw new HandleException(ErrorCode.NORMAL_ERROR, "余额不足");
		}
		
		return account;
	}

	@Transactional
	public Account lockCash(Integer uid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		if(account.getCash()>=amount){
			account.setCash(account.getCash()-amount);
			account.setLockedcash(account.getLockedcash()+amount);
			accountMapper.updateByPrimaryKey(account);
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			//TODO：记录账户记录
		}else{
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			throw new HandleException(ErrorCode.NORMAL_ERROR, "余额不足");
		}
		
		return account;
	}

	@Transactional
	public Account unlockCash(Integer uid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		if(account.getLockedcash()>=amount){
			account.setCash(account.getCash()+amount);
			account.setLockedcash(account.getLockedcash()-amount);
			accountMapper.updateByPrimaryKey(account);
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			//TODO：记录账户记录
		}else{
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			throw new HandleException(ErrorCode.NORMAL_ERROR, "余额不足");
		}
		
		return account;
	}

	@Transactional
	public void payCash(Integer uid, Integer touid, int amount, String msg) {
		redissonUtil.tryLock("USER_ACCOUNT_"+uid, TimeUnit.MILLISECONDS, 300, 300);
		Account account = getMyAccount(uid);
		if(account.getLockedcash()>=amount){
			account.setLockedcash(account.getLockedcash()-amount);
			accountMapper.updateByPrimaryKey(account);
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			//TODO：记录账户记录
		}else{
			redissonUtil.unlock("USER_ACCOUNT_"+uid);
			throw new HandleException(ErrorCode.NORMAL_ERROR, "余额不足");
		}
		
		addCash(touid, amount, msg);
		
		return;
	}

	
}
