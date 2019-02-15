import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class PartOneTestCases {

    public static final double DELTA = 0.00001;

    @Test
    public void testPerimeterCircle1() {
        assertEquals(2 * Math.PI,
                Util.perimeter(new Circle(new Point(0.0, 0.0), 1.0)),
                DELTA);
    }

    @Test
    public void testPerimeterCircle2() {
        assertEquals(0.0,
                Util.perimeter(new Circle(new Point(0.0, 0.0), 0.0)),
                DELTA);
    }

    @Test
    public void testPerimeterRectangle1() {
        assertEquals(14.0,
                Util.perimeter(new Rectangle(
                        new Point(0.0, 0.0),
                        new Point(3.0, 4.0)
                )),
                DELTA);
    }

    @Test
    public void testPerimeterRectangle2() {
        assertEquals(0.0,
                Util.perimeter(new Rectangle(
                        new Point(0.0, 0.0),
                        new Point(0.0, 0.0)
                )),
                DELTA);
    }

    @Test
    public void testPerimeterPolygon1() {
        assertEquals(12.0,
                Util.perimeter(new Polygon(Arrays.asList(
                                new Point(0.0, 0.0),
                                new Point(3.0, 0.0),
                                new Point(3.0, 4.0)
                ))),
                DELTA);
    }

    @Test
    public void testPerimeterPolygon2() {
        assertEquals(0.0,
                Util.perimeter(new Polygon(Arrays.asList(
                        new Point(0.0, 0.0)
                ))),
                DELTA);
    }

    @Test
    public void testWhichIsBigger1() {
        assertEquals(25.132741228718345,
                Bigger.whichIsBigger(
                        new Circle(new Point(0.0, 0.0), 2.0),
                        new Rectangle(new Point(-1.0, 2.0), new Point(1.0, -1.6)),
                        new Polygon(Arrays.asList(
                                new Point(0.0, 0.0),
                                new Point(3.0, 1.0),
                                new Point(1.0, 4.0),
                                new Point(-1.0, 4.0)
                        ))
                ),
                DELTA);
    }

    @Test
    public void testWhichIsBigger2() {
        assertEquals(0.0,
                Bigger.whichIsBigger(
                        new Circle(new Point(0.0, 0.0), 0.0),
                        new Rectangle(new Point(0.0, 0.0), new Point(0.0, 0.0)),
                        new Polygon(Arrays.asList(new Point(0.0, 0.0)))
                ),
                DELTA);
    }


    @Test
    public void testCircleImplSpecifics()
        throws NoSuchMethodException
    {
        final List<String> expectedMethodNames = Arrays.asList(
            "getCenter", "getRadius");

        final List<Class> expectedMethodReturns = Arrays.asList(
            Point.class, double.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(
            new Class[0], new Class[0]);

        verifyImplSpecifics(Circle.class, expectedMethodNames,
            expectedMethodReturns, expectedMethodParameters);
    }

    @Test
    public void testRectangleImplSpecifics()
        throws NoSuchMethodException
    {
        final List<String> expectedMethodNames = Arrays.asList(
            "getTopLeft", "getBottomRight");

        final List<Class> expectedMethodReturns = Arrays.asList(
            Point.class, Point.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(
            new Class[0], new Class[0]);

        verifyImplSpecifics(Rectangle.class, expectedMethodNames,
            expectedMethodReturns, expectedMethodParameters);
    }

    @Test
    public void testPolygonImplSpecifics()
        throws NoSuchMethodException
    {
        final List<String> expectedMethodNames = Arrays.asList(
            "getPoints");

        final List<Class> expectedMethodReturns = Arrays.asList(
            List.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(
            new Class[][] {new Class[0]});

        verifyImplSpecifics(Polygon.class, expectedMethodNames,
            expectedMethodReturns, expectedMethodParameters);
    }

    @Test
    public void testUtilImplSpecifics()
        throws NoSuchMethodException
    {
        final List<String> expectedMethodNames = Arrays.asList(
            "perimeter", "perimeter", "perimeter");

        final List<Class> expectedMethodReturns = Arrays.asList(
            double.class, double.class, double.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(
            new Class[] {Circle.class},
            new Class[] {Polygon.class},
            new Class[] {Rectangle.class});

        verifyImplSpecifics(Util.class, expectedMethodNames,
            expectedMethodReturns, expectedMethodParameters);
    }

    private static void verifyImplSpecifics(
        final Class<?> clazz,
        final List<String> expectedMethodNames,
        final List<Class> expectedMethodReturns,
        final List<Class[]> expectedMethodParameters)
        throws NoSuchMethodException
    {
        assertEquals("Unexpected number of public fields",
            0, Point.class.getFields().length);

        final List<Method> publicMethods = Arrays.stream(
            clazz.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .collect(Collectors.toList());

        assertEquals("Unexpected number of public methods",
            expectedMethodNames.size(), publicMethods.size());

        assertTrue("Invalid test configuration",
            expectedMethodNames.size() == expectedMethodReturns.size());
        assertTrue("Invalid test configuration",
            expectedMethodNames.size() == expectedMethodParameters.size());

        for (int i = 0; i < expectedMethodNames.size(); i++)
        {
            Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i),
                expectedMethodParameters.get(i));
            assertEquals(expectedMethodReturns.get(i), method.getReturnType());
        }
    }
}
