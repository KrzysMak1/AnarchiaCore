package cc.dreamcode.antylogout.libs.cc.dreamcode.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object.Duo;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Executor;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.List;
import lombok.NonNull;

public interface CommandBase
{
    default List<CommandPathMeta> getCommandPaths(@NonNull final CommandMeta commandMeta) {
        if (commandMeta == null) {
            throw new NullPointerException("commandMeta is marked non-null but is null");
        }
        final ListBuilder<CommandPathMeta> executors = new ListBuilder<CommandPathMeta>();
        final Map<String, Duo<Integer, Integer>> usedPaths = (Map<String, Duo<Integer, Integer>>)new HashMap();
        for (final Method declaredMethod : this.getClass().getDeclaredMethods()) {
            final Executor executor = (Executor)declaredMethod.getAnnotation((Class)Executor.class);
            if (executor != null) {
                for (final String path : executor.path()) {
                    int totalArgs = 0;
                    int totalOptArgs = 0;
                    for (final Annotation[] array : declaredMethod.getParameterAnnotations()) {
                        final Annotation[] parameterAnnotation = array;
                        for (final Annotation annotation : array) {
                            if (Arg.class.isAssignableFrom(annotation.annotationType())) {
                                ++totalArgs;
                            }
                            if (OptArg.class.isAssignableFrom(annotation.annotationType())) {
                                ++totalOptArgs;
                            }
                        }
                    }
                    final Duo<Integer, Integer> value = (Duo<Integer, Integer>)usedPaths.get((Object)path);
                    if (value != null && value.getFirst() == totalArgs && value.getSecond() == totalOptArgs) {
                        throw new RuntimeException("duplicate path provided: " + path);
                    }
                    usedPaths.put((Object)path, (Object)Duo.of(totalArgs, totalOptArgs));
                }
            }
        }
        for (final Method declaredMethod : this.getClass().getDeclaredMethods()) {
            final Executor executor = (Executor)declaredMethod.getAnnotation((Class)Executor.class);
            if (executor != null) {
                declaredMethod.setAccessible(true);
                for (final String path : executor.path()) {
                    final CommandPathMeta commandPathMeta = new CommandPathMeta(commandMeta, declaredMethod, path, executor.description());
                    executors.add(commandPathMeta);
                }
            }
        }
        return executors.build();
    }
}
