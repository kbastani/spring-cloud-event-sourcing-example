package demo;


import demo.catalog.CatalogInfo;
import demo.catalog.CatalogInfoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CatalogServiceApplication.class)
@ActiveProfiles(profiles = "test")
@WebIntegrationTest
public class CatalogServiceApplicationTests {

    @Autowired
    private CatalogInfoRepository catalogInfoRepository;

    @Test
    public void createCatalogInfo() throws Exception {
        catalogInfoRepository.deleteAll();
        CatalogInfo catalogInfo = new CatalogInfo(0L, true);
        catalogInfoRepository.save(catalogInfo);

        CatalogInfo actual = catalogInfoRepository.findCatalogByActive(true);

        Assert.assertEquals(catalogInfo, actual);
    }
}
