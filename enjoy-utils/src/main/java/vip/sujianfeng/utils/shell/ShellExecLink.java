package vip.sujianfeng.utils.shell;


import vip.sujianfeng.utils.define.CallResult;

import java.io.File;

/**
 * author SuJianFeng
 * createTime  2019/7/30 17:48
 * 链式执行shell脚本
 **/
public class ShellExecLink {

    private ShellExec shellExec = new ShellExec();

    public ShellExecLink exec(CallResult<?> opResult, String var1, String... vars){
        return this.exec(opResult, 1000, var1, vars, null);
    }

    public ShellExecLink exec(CallResult<?> opResult, int waitMilliSeconds, String var1, String[] vars, File file){
        if (!opResult.isSuccess()) return this;
        shellExec.exec(opResult, waitMilliSeconds, var1, vars, file);
        return this;
    }

    public ShellExec getShellExec() {
        return shellExec;
    }

    public void setShellExec(ShellExec shellExec) {
        this.shellExec = shellExec;
    }
}
