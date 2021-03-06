package org.junita.model;


import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junita.core.EnclosedTest;
import org.junita.core.Enclosure;
import org.junita.model.collection.TargetAggregate;

/**
 * A collection of all enclosed tests
 *
 * @author : Balaji Narain
 */
public class AllEnclosedTests extends TargetAggregate<Class<?>> {

    private Enclosure enclosure;

    AllEnclosedTests(TestClass testClass) {
        for (Class clazz : testClass.clazz().getClasses()) {
            if (clazz.isAnnotationPresent(EnclosedTest.class)) {
                this.add(clazz);
            }
        }
    }

    public AllEnclosedTests(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    @Override
    public void describe(Description suiteDescription, Class clazz) {
        for (Class<?> target : invokables) {
            TestClass innerTest = new TestClass(target);
            Description description = enclosure(innerTest).getDescription();
            suiteDescription.addChild(description);
        }
    }

    @Override
    public boolean run(Class<?> innerClass, Object enclosingInstance, Description description, RunNotifier notifier) throws Exception {
        for (Class<?> target : invokables) {
            description.addChild(Description.createSuiteDescription(innerClass));
            TestClass innerTest = new TestClass(target, enclosingInstance);
            enclosure(innerTest).run(notifier);
        }
        return true;
    }

    private Enclosure enclosure(TestClass target) {
        if (enclosure != null)
            return enclosure;
        return new Enclosure(target);
    }
}
