package com.yao.jmh.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by yaozb on 15-4-29.
 */
@State(Scope.Thread)
public class JMHSample_06_FixtureLevel {

    double x;

    /*
     * Fixture methods have different levels to control when they are about to run.
     * There are at least three Levels available at user expense. These are, from
     * the top to bottom:
     *
     * Level.Trial:      before or after the entire benchmark run (the sequence of iterations)
     * Level.Iteration:  before or after the benchmark iteration (the sequence of invocations)
     * Level.Invocation; before or after the benchmark method invocation (WARNING: read the Javadoc before using)
     *
     * Time spent in fixture methods does not count into the performance metrics,
     * so you can use this to do some heavy-lifting.
     */

    @TearDown(Level.Iteration)
    public void check() {
        assert x > Math.PI : "Nothing changed?";
    }

    @Benchmark
    public void measureRight() {
        x++;
    }

    @Benchmark
    public void measureWrong() {
        double x = 0;
        x++;
    }

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * You can see measureRight() yields the result, and measureWrong() fires
     * the assert at the end of first iteration! This will not generate the results
     * for measureWrong(). You can also prevent JMH for proceeding further by
     * requiring "fail on error".
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -ea -jar target/benchmarks.jar JMHSample_06 -wi 5 -i 5 -f 1
     *    (we requested 5 warmup/measurement iterations, single fork)
     *
     *    You can optionally supply -foe to fail the complete run.
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_06_FixtureLevel.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .jvmArgs("-ea")
                .shouldFailOnError(false) // switch to "true" to fail the complete run
                .build();

        new Runner(opt).run();
    }



}
