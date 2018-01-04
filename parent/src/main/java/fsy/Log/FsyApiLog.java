package fsy.log;

import fsy.utils.Const;
import org.aspectj.lang.JoinPoint;

/**
 * @auth zln
 * @time 2017/11/28.
 */

public class FsyApiLog {

    public void AfterReturningLog(JoinPoint jp) {

    }

    public void AfterThrowingLog(JoinPoint jp,Throwable e) {
        Const.logger.error( Const.Log(jp)+"\n"+e.toString());
    }

    public void After(JoinPoint jp) {
        Const.logger.info( Const.Log(jp));

    }

    public void Before(JoinPoint jp) {

    }

}
