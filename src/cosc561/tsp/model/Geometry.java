package cosc561.tsp.model;

public class Geometry {

    public static final double EPSILON = 0.000001;

    /**
     * Calculate the cross product of two Nodes.
     * @param a first Node
     * @param b second Node
     * @return the value of the cross product
     */
    public static double crossProduct(Node a, Node b) {
        return a.x * b.y - b.x * a.y;
    }

    /**
     * Check if bounding boxes do intersect. If one bounding box
     * touches the other, they do intersect.
     * @param a first bounding box
     * @param b second bounding box
     * @return <code>true</code> if they intersect,
     *         <code>false</code> otherwise.
     */
    public static boolean doBoundingBoxesIntersect(Edge a, Edge b) {
        return a.start.x <= b.end.x && a.end.x >= b.start.x && a.start.y <= b.end.y
                && a.end.y >= b.start.y;
    }

    /**
     * Checks if a Node is on a line
     * @param a line (interpreted as line, although given as line
     *                segment)
     * @param b Node
     * @return <code>true</code> if Node is on line, otherwise
     *         <code>false</code>
     */
    public static boolean isNodeOnLine(Edge a, Node b) {
        // Move the image, so that a.start is on (0|0)
        Edge aTmp = new Edge(new Node(0, 0), new Node(
                a.end.x - a.start.x, a.end.y - a.start.y));
        Node bTmp = new Node(b.x - a.start.x, b.y - a.start.y);
        double r = crossProduct(aTmp.end, bTmp);
        return Math.abs(r) < EPSILON;
    }

    /**
     * Checks if a Node is right of a line. If the Node is on the
     * line, it is not right of the line.
     * @param a line segment interpreted as a line
     * @param b the Node
     * @return <code>true</code> if the Node is right of the line,
     *         <code>false</code> otherwise
     */
    public static boolean isNodeRightOfLine(Edge a, Node b) {
        // Move the image, so that a.start is on (0|0)
        Edge aTmp = new Edge(new Node(0, 0), new Node(
                a.end.x - a.start.x, a.end.y - a.start.y));
        Node bTmp = new Node(b.x - a.start.x, b.y - a.start.y);
        return crossProduct(aTmp.end, bTmp) < 0;
    }

    /**
     * Check if line segment first touches or crosses the line that is
     * defined by line segment second.
     *
     * @param first line segment interpreted as line
     * @param second line segment
     * @return <code>true</code> if line segment first touches or
     *                           crosses line second,
     *         <code>false</code> otherwise.
     */
    public static boolean EdgeTouchesOrCrossesLine(Edge a,
            Edge b) {
        return isNodeOnLine(a, b.start)
                || isNodeOnLine(a, b.end)
                || (isNodeRightOfLine(a, b.start) ^ isNodeRightOfLine(a,
                        b.end));
    }

    /**
     * Check if line segments intersect
     * @param a first line segment
     * @param b second line segment
     * @return <code>true</code> if lines do intersect,
     *         <code>false</code> otherwise
     */
    public static boolean doLinesIntersect(Edge a, Edge b) {
        return doBoundingBoxesIntersect(a, b)
                && EdgeTouchesOrCrossesLine(a, b)
                && EdgeTouchesOrCrossesLine(b, a);
    }
}