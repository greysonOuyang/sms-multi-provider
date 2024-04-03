package com.sms.test.service;

import com.sms.api.domain.SmsTemplateEntity;
import com.sms.service.templates.ConfigFileTemplateConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

// 模拟ApplicationContext以供测试使用
public class ConfigFileTemplateConfigurationTest {

    private ConfigFileTemplateConfiguration configFileTemplateConfiguration;
    private ApplicationContext applicationContextMock;

    @Before
    public void setup() {
        applicationContextMock = Mockito.mock(ApplicationContext.class);
        Mockito.when(applicationContextMock.getBean(ConfigFileTemplateConfiguration.class))
                .thenReturn(new ConfigFileTemplateConfiguration());

        configFileTemplateConfiguration = applicationContextMock.getBean(ConfigFileTemplateConfiguration.class);
    }

    @Test
    public void testLoadTemplatesFromFile() {
        // 测试加载模板文件的功能
        configFileTemplateConfiguration.loadTemplatesFromFile();
        Assert.assertNotNull("The config should be loaded", configFileTemplateConfiguration.getConfig());
    }

    @Test
    public void testRefresh() {
        // 测试自动刷新功能
        Long preUpdateTime = configFileTemplateConfiguration.getConfig().getLastUpdateTime();
        configFileTemplateConfiguration.refresh();
        Long postUpdateTime = configFileTemplateConfiguration.getConfig().getLastUpdateTime();
        Assert.assertTrue("The configuration should be refreshed", postUpdateTime > preUpdateTime);
    }

    @Test
    public void testGetTemplate() {
        // 测试获取模板的逻辑
        String businessCode = "testBusinessCode";
        String provider = "testProvider";
        SmsTemplateEntity template = configFileTemplateConfiguration.getTemplate(businessCode, provider);
        Assert.assertNotNull("The template should not be null", template);
        Assert.assertEquals("The business code should match", businessCode, template.getBusinessCode());
        Assert.assertEquals("The provider should match", provider, template.getSmsProvider());
    }
}