package ${packageName};

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyTransform  {
    private static final transient Log LOG = LogFactory.getLog(MyTransform.class);
    private boolean verbose = true;
    private String prefix = "MyTransform";

    public Object transform(Object body) {
        String answer = prefix + " set body:  " + new Date();
        if (verbose) {
            System.out.println(">>>> " + answer);
        }
        LOG.info(">>>> " + answer);
        return answer;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
