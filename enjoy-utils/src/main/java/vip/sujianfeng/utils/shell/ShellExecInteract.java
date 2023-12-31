package vip.sujianfeng.utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Shell Interaction Operations
 * author SuJianFeng
 * createTime  2023/5/5
 **/
public class ShellExecInteract {
    private static final Logger logger = LoggerFactory.getLogger(ShellExecInteract.class);

    private final Charset chatSet = StandardCharsets.UTF_8;

    private final Process ps;
    private final ShellExecInteractStream errorGobbler;
    private final ShellExecInteractStream stdoutGobbler;
    private ShellExecInteractStream.CallBack callBack;

    public ShellExecInteract(String command, ShellExecInteractStream.CallBack callBack) throws IOException {
        ps = Runtime.getRuntime().exec(command);
        logger.info("start => {}", command);
        this.callBack = callBack;
        callBack.call(this, ShellExecInteractStream.STDOUT, command);
        errorGobbler = new ShellExecInteractStream(this, ps.getErrorStream(), callBack, ShellExecInteractStream.ERROR, chatSet.name());
        errorGobbler.start();
        stdoutGobbler = new ShellExecInteractStream(this, ps.getInputStream(), callBack, ShellExecInteractStream.STDOUT, chatSet.name());
        stdoutGobbler.start();
    }

    public ShellExecInteract outputCommand(String command) throws IOException {
        logger.info("command => {}", command);
        this.callBack.call(this, ShellExecInteractStream.STDOUT, command);
        ps.getOutputStream().write(String.format("%s \r\n", command).getBytes(chatSet));
        ps.getOutputStream().flush();
        return this;
    }

    public Process getPs() {
        return ps;
    }

    /**
     * Close child processes
     */
    public void close() {
        ps.destroy();
    }

}
