package com.xiangkai.community.util;

import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class LocalCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCommandExecutor.class);

    public static <T> T execute(String command, CommandCallback<T> callback) {
        try {
            return callback.doWithProcessor(new Processor(command));
        } catch (Exception e) {
            LOGGER.error("执行命令出错：\n" + e);
            return null;
        }
    }

    public static class CommandExecuteResult {

        private String error;

        private String input;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    public interface CommandCallback<T> {
        T doWithProcessor(Processor processor) throws Exception;
    }

    public static class Processor {

        private final String command;

        private final CommandExecuteResult result;

        public Processor(String command) {
            this.command = command;
            this.result = new CommandExecuteResult();
        }

        private void process() throws Exception {

            Process process = Runtime.getRuntime().exec(command);
            boolean waitFor = process.waitFor(30, TimeUnit.SECONDS);

            // 超时未完成
            if (!waitFor) {
                process.destroy();
                return;
            }

            try (StringBuilderWriter errorSw = new StringBuilderWriter();
                 InputStreamReader errorReader = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);

                 StringBuilderWriter inputSw = new StringBuilderWriter();
                 InputStreamReader inputReader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
            ) {
                char[] buffer = new char[1024];
                int n;
                while (-1 != (n =errorReader.read(buffer))) {
                    errorSw.write(buffer, 0, n);
                }
                result.setError(errorSw.toString());

                while (-1 != (n =inputReader.read(buffer))) {
                    inputSw.write(buffer, 0, n);
                }
                result.setInput(inputSw.toString());
            }
        }

        public CommandExecuteResult result() throws Exception {
            process();
            return result;
        }
    }
}
