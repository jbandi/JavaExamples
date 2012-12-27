package net.jonasbandi;

import org.junit.Assert;
import org.junit.Test;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class EarScannerTest {

    private String STUB_JBOSS_VFSURL = "vfsfile:" + getProjectBaseDir() + "/../testdata/target/cpscanner-ear-1.0-SNAPSHOT.ear/META-INF/application.xml";
    private String EXPLODED_EAR_PATH = getProjectBaseDir() + "/../testdata/target/cpscanner-ear-1.0-SNAPSHOT";
    private String ARCHIVED_EAR_PATH = getProjectBaseDir() + "/../testdata/target/cpscanner-ear-1.0-SNAPSHOT.ear";

    @Test
    public void scanEar() throws IOException {

        EarScanner.EarPathHelper stubEarPathHelper = new EarScanner.EarPathHelper() {
            String getApplicationXmResourcelUrl(){
                return STUB_JBOSS_VFSURL;
            }
        };

        EarScanner earScanner = new EarScanner(stubEarPathHelper);
        List<String> classList =  earScanner.getClassesAnnotatedWith(Stateless.class);

        Assert.assertThat(classList.size(), is(1));
    }

    @Test
    public void scanExplodedEar() throws IOException {

        EarScanner.EarPathHelper stubEarPathHelper = new EarScanner.EarPathHelper() {
            @Override String getApplicationXmResourcelUrl(){
                return STUB_JBOSS_VFSURL;
            }
            @Override String getEarPathFromResourceUrl(String resourceUrl){
                return EXPLODED_EAR_PATH;
            }
        };

        EarScanner earScanner = new EarScanner(stubEarPathHelper);
        List<String> classList =  earScanner.getClassesAnnotatedWith(Stateless.class);

        Assert.assertThat(classList.size(), is(1));
    }

    @Test
    public void getEarPathFromResourceUrl(){
        EarScanner.EarPathHelper earPathHelper = new EarScanner.EarPathHelper();
        String earPath = earPathHelper.getEarPathFromResourceUrl(STUB_JBOSS_VFSURL);
        Assert.assertEquals(getProjectBaseDir() + "/../testdata/target/cpscanner-ear-1.0-SNAPSHOT.ear", earPath);
    }

    @Test
    public void isEarExploded(){
        EarScanner.EarPathHelper earPathHelper = new EarScanner.EarPathHelper();

        boolean isExploded = earPathHelper.isExplodedEar(EXPLODED_EAR_PATH);
        Assert.assertEquals(true, isExploded);

        isExploded = earPathHelper.isExplodedEar(ARCHIVED_EAR_PATH);
        Assert.assertEquals(false, isExploded);
    }

    private String getProjectBaseDir(){
        File file = new File(System.getProperty("user.dir"));
        return file.getAbsolutePath();
    }
}
