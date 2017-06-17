package com.github.alexeybond.gdx_commons.game.utils.destruction.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.github.alexeybond.gdx_commons.game.utils.destruction.Destroyer;
import com.github.alexeybond.gdx_commons.game.utils.destruction.DestroyerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DestroyerImpl implements Destroyer {
    private DestroyerConfig config = new DestroyerConfig();

    @Override
    public DestroyerConfig config() {
        return config;
    }

    private void checkConfigValue(boolean expr, String msg) {
        if (!expr) {
            throw new IllegalStateException("Destroyer configuration assertion failed: " + msg);
        }
    }

    private void checkConfig() {
        DestroyerConfig config = this.config;
        checkConfigValue(config.minTriArea > 0f, "minTriArea > 0");
        checkConfigValue(config.initialRaysMin >= 5, "initialRaysMin >= 5");
        checkConfigValue(config.initialRaysMax >= config.initialRaysMin, "initialRaysMax >= initialRaysMin");
        checkConfigValue(config.crackLengthMin > 0f, "crackLengthMin > 0");
        checkConfigValue(config.crackLengthMax >= config.crackLengthMin, "crackLengthMax >= crackLengthMin");
        checkConfigValue(config.forkRaysMin >= 2, "forkRaysMin >= 2");
        checkConfigValue(config.forkRaysMax >= config.forkRaysMin, "forkRaysMax >= forkRaysMin");
        checkConfigValue((config.forkAngleRange / (float) config.forkRaysMin) * 2f < 180f,
                "(forkAngleRange / forkRaysMin) * 2 < 180");
        checkConfigValue(config.forkAngleRestrictRangeFraction > 0, "forkAngleRestrictRangeFraction > 0");
        checkConfigValue(config.forkAngleRestrictRangeFraction < 1, "forkAngleRestrictRangeFraction < 1");
    }

    private Queue<Ray> rayQueue = new Queue<Ray>();

    private final Edge.Pool edgePool = new Edge.Pool(900);

    protected Edge edge() {
        return edgePool.acquire();
    }

    protected void noEdge(Edge edge) {
    }

    protected Edge sideEdge() {
        Edge edge = edgePool.first();

        while (null != edge) {
            if (edge.order == 1) return edge;
            edge = edgePool.next(edge);
        }

        return null;
    }

    protected Vertex vertex() {
        return new Vertex().clear();
    }

    protected Ray startRay() {
        Ray ray = new Ray();
        rayQueue.addLast(ray);
        return ray;
    }

    protected Ray nextRay() {
        if (rayQueue.size == 0) return null;
        return rayQueue.removeFirst();
    }

    protected void doneRay(Ray ray) {
    }

    protected <T> ArrayList<T> list(int preferReserve) {
        return new ArrayList<T>(preferReserve);
    }

    private final ArrayList<Vertex> initialVertices = new ArrayList<Vertex>();
    private final Rectangle boundingRect = new Rectangle();

    @Override
    public void prepare(List<Vector2> shape) {
        checkConfig();

        if (shape.size() < 3) throw new IllegalArgumentException("shape.size < 3");

        initialVertices.clear();
        boundingRect.setPosition(shape.get(0)).setSize(0);
        for (Vector2 point : shape) {
            Vertex vertex = vertex();
            vertex.p.set(point);
            initialVertices.add(vertex);
            boundingRect.merge(point);
        }

        for (int i = 0; i < initialVertices.size(); i++) {
            int j = (i == 0) ? initialVertices.size() - 1 : i - 1;
            edge().set(initialVertices.get(j), initialVertices.get(i), 1);
        }
    }

    private void startRays(Vertex vertex, Vector2 baseDir, float range, int n) {
        float sector = range * 2f / (float) n;
        float halfSectorUse = .5f * (1f - config.forkAngleRestrictRangeFraction);

        for (int i = 0; i < n; i++) {
            float aAvg = sector * ((-.5f * (float) (n - 1)) + (float) i);
            float a = MathUtils.random(aAvg - sector * halfSectorUse, aAvg + sector * halfSectorUse);
            float len = MathUtils.random(config.crackLengthMin, config.crackLengthMax);

            Ray ray = startRay().set(vertex, baseDir, len);

            ray.dir.rotate(a);
        }
    }

    @Override
    public void startCenter() {
        Vertex start = vertex();
        boundingRect.getCenter(start.p);

        int initialRays = MathUtils.random(config.initialRaysMin, config.initialRaysMax);
        startRays(start, Vector2.Y, 180, initialRays);
    }

    @Override
    public void startEdge(Vector2 rayStart, Vector2 rayDir) {
        throw new UnsupportedOperationException();
    }

    private void traceCracks() {
        Ray ray;

        while (null != (ray = nextRay())) {
            Edge bestEdge = null;
            float bestDistance = ray.maxLen * 2f;

            Edge edge = edgePool.first();
            while (null != edge) {
                float distance = ray.intersection(edge, bestDistance);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestEdge = edge;
                }

                edge = edgePool.next(edge);
            }

            if (null == bestEdge) {
                Vertex end = ray.skip(ray.maxLen, vertex());

                edge().set(ray.start, end, 2);

                int nRays = MathUtils.random(config.forkRaysMin, config.forkRaysMax);
                startRays(end, ray.dir, config.forkAngleRange, nRays);
            } else {
                Vertex mid = ray.skip(bestDistance, vertex());
                Vertex v1 = bestEdge.v1;
                Vertex v2 = bestEdge.v2;
                int order = bestEdge.order;
                bestEdge.dispose();
                noEdge(bestEdge);

                edge().set(v1, mid, order);
                edge().set(mid, v2, order);
                edge().set(ray.start, mid, 2);
            }

            doneRay(ray);
        }
    }

    private void mark(Vertex vertex, float mark, int imark) {
        if (vertex.markIter == imark && vertex.markValue <= mark) return;

        vertex.markIter = imark;
        vertex.markValue = mark;

        for (int i = 0; i < vertex.edges.size; i++) {
            Edge edge = vertex.edges.get(i);
            mark(edge.other(vertex), edge.len + mark, imark);
        }
    }

    private void walk(Vertex from, Vertex to, List<Edge> edges, List<Vertex> vertices) {
        vertices.add(from);

        if (from == to) return;

        Edge bestE = null;
        Vertex bestV = null;
        float bestMark = from.markValue;

        for (int i = 0; i < from.edges.size; i++) {
            Edge cur = from.edges.get(i);
            Vertex cv = cur.other(from);

            if (cv.markValue < bestMark) {
                bestMark = cv.markValue;
                bestE = cur;
                bestV = cv;
            }
        }

        edges.add(bestE);

        walk(bestV, to, edges, vertices);
    }

    private void shortestPath(
            Vertex start, Vertex end,
            List<Edge> edges, List<Vertex> vertices,
            int imark) {
        mark(end, 0, imark);
        walk(start, end, edges, vertices);
    }

    private boolean verifyPartShape(List<Vertex> vertices, List<Vector2> points) {
        for (int aIdx = 0; aIdx < vertices.size(); aIdx++) {
            int bIdx = aIdx - 1; bIdx = (bIdx < 0) ? vertices.size() + bIdx : bIdx;
            int cIdx = aIdx - 2; cIdx = (cIdx < 0) ? vertices.size() + cIdx : cIdx;

            Vector2 a = vertices.get(aIdx).p;
            Vector2 b = vertices.get(bIdx).p;
            Vector2 c = vertices.get(cIdx).p;

            float area = Math.abs((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x));

            if (area >= config.minTriArea) {
                points.add(b);
            }
        }

        return points.size() >= 3;
    }

    private void extractPolygons(ArrayList<ArrayList<Vector2>> dst) {
        int iter = 1;
        Edge side;

        ArrayList<Edge> xEdges = list(16);
        ArrayList<Vertex> xVertices = list(16);
        ArrayList<Vector2> resShape = null;
        //*
        while (null != (side = sideEdge())) {
            xEdges.clear();
            xVertices.clear();

            Vertex start = side.v1;
            Vertex end = side.v2;
            side.dispose();
            noEdge(side);

            shortestPath(start, end, xEdges, xVertices, ++iter);

            if (null == resShape) resShape = list(xVertices.size());

            if (verifyPartShape(xVertices, resShape)) {
                dst.add(resShape);
                resShape = null;
            } else {
                resShape.clear();
            }

            for (int i = 0; i < xEdges.size(); i++) {
                Edge edge = xEdges.get(i);

                if (edge.disorder()) noEdge(edge);
            }
        }
        /*/
        for (Edge edge : edges) {
            if (edge.v2.p.dst2(edge.v1.p) < 0.1) continue;
            Vector2 third = new Vector2(edge.v1.p).add(edge.v2.p).scl(.5f);
            Vector2 fourth = new Vector2(edge.v1.p).add(edge.v2.p).scl(.5f);
            Vector2 d = new Vector2(edge.v1.p).sub(edge.v2.p).rotate(90).nor().scl(2);
            third.add(d); fourth.sub(d);
            dst.add(new ArrayList<Vector2>(Arrays.asList(edge.v1.p, fourth, edge.v2.p, third)));
        }
        //*/
    }

    @Override
    public ArrayList<ArrayList<Vector2>> compute() {
        long sTime = System.nanoTime();
        traceCracks();

        long mTime = System.nanoTime();

        ArrayList<ArrayList<Vector2>> res = list(16);
        extractPolygons(res);

        long eTime = System.nanoTime();

        Gdx.app.log("DESTROYER", "Destroyed in " + (0.000001 * (double)(eTime - sTime))
                    + " " + (100.0 * ((double)(mTime - sTime)) / ((double)(eTime - sTime)))
                    + "% for trace.");

        return res;
    }

    @Override
    public void reset() {
        edgePool.releaseAll();
    }
}
