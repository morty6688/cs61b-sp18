public class KdTree {
    private KdNode root;
    private int size;

    private static class KdNode {
        private final GraphDB.Node point;
        private boolean compareX;
        private KdNode leftBottom;
        private KdNode rightTop;

        KdNode(GraphDB.Node p, boolean compareX) {
            this.point = p;
            this.compareX = compareX;
        }

        public boolean isRightOrTopOf(GraphDB.Node q) {
            return (compareX && point.lon > q.lon || (!compareX && point.lat > q.lat));
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(GraphDB.Node p) {
        if (root == null) {
            root = new KdNode(p, true);
            size++;
            return;
        }

        // find node position for insertion
        KdNode pre = null;
        KdNode cur = root;
        do {
            if (cur.point.id == p.id) {
                return;
            }
            pre = cur;
            cur = cur.isRightOrTopOf(p) ? cur.leftBottom : cur.rightTop;
        } while (cur != null);

        // prepare new node and insert
        if (pre.isRightOrTopOf(p)) {
            pre.leftBottom = new KdNode(p, !pre.compareX);
        } else {
            pre.rightTop = new KdNode(p, !pre.compareX);
        }
        size++;
    }

    public long nearest(double lon, double lat) {
        return nearest(new GraphDB.Node(0, lon, lat)).id;
    }

    public GraphDB.Node nearest(GraphDB.Node p) {
        return nearest(p, root, Double.MAX_VALUE);
    }

    private GraphDB.Node nearest(GraphDB.Node p, KdTree.KdNode node, double minDist) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (node.point == null) {
            return null;
        }
        if (node.point.equals(p)) {
            return node.point;
        }
        GraphDB.Node bestPoint = null;
        double bestDist = minDist;
        double nodeDist = GraphDB.distance(node.point.lon, node.point.lat, p.lon, p.lat);
        if (nodeDist < minDist) {
            bestPoint = node.point;
            bestDist = nodeDist;
        }
        KdNode first = node.rightTop, second = node.leftBottom;
        if ((node.compareX && p.lon <= node.point.lon)
                || (!node.compareX && p.lat <= node.point.lat)) {
            first = node.leftBottom;
            second = node.rightTop;
        }
        if (first != null) {
            GraphDB.Node firstBestPoint = nearest(p, first, bestDist);
            if (firstBestPoint != null) {
                bestPoint = firstBestPoint;
                bestDist = GraphDB.distance(bestPoint.lon, bestPoint.lat, p.lon, p.lat);
            }
        }
        if (second == null) {
            return bestPoint;
        }

        if (second == node.leftBottom) {
            if (node.compareX && p.lon - node.point.lon >= bestDist) {
                return bestPoint;
            }
            if (!node.compareX && p.lat - node.point.lat >= bestDist) {
                return bestPoint;
            }
        } else if (second == node.rightTop) {
            if (node.compareX && node.point.lon - p.lon >= bestDist) {
                return bestPoint;
            }
            if (!node.compareX && node.point.lat - p.lat >= bestDist) {
                return bestPoint;
            }
        }
        GraphDB.Node secondBestPoint = nearest(p, second, bestDist);
        if (secondBestPoint != null) {
            bestPoint = secondBestPoint;
        }
        return bestPoint;
    }
}
