//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//import controllers.Application;
//
////@ContextConfiguration(classes = {TestAppConfig.class, TestDataConfig.class})
//@TransactionConfiguration(defaultRollback=true)
//@Transactional // Automatically preconfigured by extending the class below
//public abstract class BaseTestCase extends AbstractTransactionalJUnit4SpringContextTests {
//    @Autowired
//    private Application appHandler;
//
//    private static final Long userId = 3220L;
//
//    @Before
//    public void before() {
//        // applicationContext is available due to extending the Spring class
//        ContextManager.start(applicationContext);
//
//        // Create fake Session data for the tests.
//        Application appHandler = ContextManager.getContext().getBean(Application.class);
//        //final String globalUuid = SessionRepositoryI.GLOBAL_UUID_PREFIX + userId + "-" + UUID.randomUUID().toString() + GlobalSessionHandler.SESSION_UUID_SUFFIX;
//        //TestAppConfig.testSession = sessionHandler.createSession(globalUuid, userId, "CONTRACTOR");
//    }
//
//    @After
//    public void after() {
//        // XXX - Can't be done since the Spring JUnit runner won't't recreate the context
//        // for subsequent tests methods or classes.
////        ContextManager.stop();
//    }
//}
