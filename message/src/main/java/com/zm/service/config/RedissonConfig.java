package com.zm.service.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonConfig.RedissonProperties.class)
public class RedissonConfig {

	@ConfigurationProperties(prefix = "redisson")
	@ConditionalOnProperty("redisson.password")
	public class RedissonProperties {

	    private int timeout = 3000;

	    private String address;

	    private String password;
	    
	    private int database = 0;

	    private int connectionPoolSize = 64;
	    
	    private int connectionMinimumIdleSize=10;

	    private int slaveConnectionPoolSize = 250;

	    private int masterConnectionPoolSize = 250;

	    private String[] sentinelAddresses;

	    private String masterName;

	    public int getTimeout() {
	        return timeout;
	    }

	    public void setTimeout(int timeout) {
	        this.timeout = timeout;
	    }

	    public int getSlaveConnectionPoolSize() {
	        return slaveConnectionPoolSize;
	    }

	    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
	        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
	    }

	    public int getMasterConnectionPoolSize() {
	        return masterConnectionPoolSize;
	    }

	    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
	        this.masterConnectionPoolSize = masterConnectionPoolSize;
	    }

	    public String[] getSentinelAddresses() {
	        return sentinelAddresses;
	    }

	    public void setSentinelAddresses(String sentinelAddresses) {
	        this.sentinelAddresses = sentinelAddresses.split(",");
	    }

	    public String getMasterName() {
	        return masterName;
	    }

	    public void setMasterName(String masterName) {
	        this.masterName = masterName;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public int getConnectionPoolSize() {
	        return connectionPoolSize;
	    }

	    public void setConnectionPoolSize(int connectionPoolSize) {
	        this.connectionPoolSize = connectionPoolSize;
	    }

	    public int getConnectionMinimumIdleSize() {
	        return connectionMinimumIdleSize;
	    }

	    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
	        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
	    }

	    public int getDatabase() {
	        return database;
	    }

	    public void setDatabase(int database) {
	        this.database = database;
	    }

	    public void setSentinelAddresses(String[] sentinelAddresses) {
	        this.sentinelAddresses = sentinelAddresses;
	    }
	}
	
	
	@Autowired
    private RedissonProperties redssionProperties;

    /**
     * 哨兵模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.master-name")
    public RedissonClient redissonSentinel() {
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redssionProperties.getSentinelAddresses())
                .setMasterName(redssionProperties.getMasterName())
                .setTimeout(redssionProperties.getTimeout())
                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize())
                .setDatabase(redssionProperties.getDatabase());                            
        
        if(StringUtils.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 单机模式自动装配
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.address")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redssionProperties.getAddress())
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());
        
        if(StringUtils.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
     */
//    @Bean
//    DistributedLocker distributedLocker(RedissonClient redissonClient) {
//        DistributedLocker locker = new DistributedLocker();
//        locker.setRedissonClient(redissonClient);
//        RedissLockUtil.setLocker(locker);
//        return locker;
//    }
}
