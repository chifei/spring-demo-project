package core.test.rest;

import core.framework.web.EnvironmentInitializer;
import core.framework.web.filter.PlatformFilter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

/**
 * @author neo
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class, initializers = EnvironmentInitializer.class)
@TransactionConfiguration
@WebAppConfiguration
public abstract class SpringTest {
    protected MockMvc mockMvc;
    @Inject
    WebApplicationContext webApplicationContext;

    @Before
    public void createMockMVC() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new PlatformFilter())
            .build();
    }
}