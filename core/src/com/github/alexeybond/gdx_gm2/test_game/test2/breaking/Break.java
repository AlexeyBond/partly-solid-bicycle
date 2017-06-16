package com.github.alexeybond.gdx_gm2.test_game.test2.breaking;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Break {
    private static class Vertex {
        Vertex(Vector2 pos) {
            this.pos.set(pos);
        }

        final Vector2 pos = new Vector2();
        List<Edge> edges = new ArrayList<Edge>();

        int imark = Integer.MIN_VALUE;
        float fmark;
    }

    private static class Edge {
        final Vertex vertex1, vertex2;
        int order;
        final float len;

        private Edge(Vertex vertex1, Vertex vertex2, int order) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            vertex1.edges.add(this);
            vertex2.edges.add(this);
            this.order = order;
            this.len = vertex1.pos.dst(vertex2.pos);
        }

        void dispose() {
            vertex1.edges.remove(this);
            vertex2.edges.remove(this);
        }

        boolean contains(Vertex vertex) {
            return vertex1 == vertex || vertex2 == vertex;
        }

        Vertex other(Vertex vertex) {
            if (vertex == vertex1) return vertex2;
            if (vertex == vertex2) return vertex1;
            throw new IllegalArgumentException();
        }
    }

    private static class BRay {
        final Vertex start;
        final Vector2 dir = new Vector2();
        final float maxLen;
        final Edge owner;

        private BRay(Vector2 dir, Vertex start1, float maxLen, Edge owner) {
            this.start = start1;
            this.maxLen = maxLen;
            this.owner = owner;
            this.dir.set(dir);
        }

        float test(Edge edge) {
            Vector2 end = new Vector2(dir)
                    .scl(maxLen * 1.2f).add(start.pos);
            Vector2 res = new Vector2();
            if (Intersector.intersectSegments(edge.vertex1.pos, edge.vertex2.pos, start.pos, end, res)) {
                return res.sub(start.pos).len();
            }

            return maxLen * 100f;
        }

        Vector2 skip(float len) {
            return new Vector2(dir).scl(len).add(start.pos);
        }
    }

    private static Vertex corner(List<Edge> edges) {
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.order > 1) continue;

            Vertex vertex = edge.vertex1;
            if (vertex.edges.size() == 2) {
                if (vertex.edges.get(0).order == 1 && vertex.edges.get(1).order == 1) return vertex;
            }
            vertex = edge.vertex2;
            if (vertex.edges.size() == 2) {
                if (vertex.edges.get(0).order == 1 && vertex.edges.get(1).order == 1) return vertex;
            }
        }

        throw new IllegalStateException("No corners.");
    }

    private static Edge side(List<Edge> edges) {
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.order != 1) continue;

            return edge;
        }

        throw new IllegalStateException("No sides.");
    }

    private static void startRays(
            Queue<BRay> q, Vector2 dir, Vertex start, float range, int n, float maxLen, Edge owner) {
        Vector2 dirr = new Vector2();

        float sector = range * 2f / (float) n;

        for (int i = 0; i < n; i++) {
            float aAvg = sector * ((-.5f * (float) (n - 1)) + (float) i);
            float a = MathUtils.random(aAvg - sector * 0.48f, aAvg + sector * 0.48f);
            q.addLast(new BRay(
                    dirr.set(dir).rotate(a),
                    start,
                    maxLen,
                    owner
            ));
        }
    }

    private static void mark(Vertex vertex, float mark, int imark) {
        if (vertex.imark == imark && vertex.fmark <= mark) return;

        vertex.imark = imark;
        vertex.fmark = mark;

        for (int i = 0; i < vertex.edges.size(); i++) {
            Edge edge = vertex.edges.get(i);
            mark(edge.other(vertex), edge.len + mark, imark);
        }
    }

    private static void walk(Vertex from, Vertex to, List<Edge> edges, List<Vertex> vertices) {
        vertices.add(from);

        if (from == to) return;

        Edge bestE = null;
        Vertex bestV = null;
        float bestMark = Float.POSITIVE_INFINITY;

        for (int i = 0; i < from.edges.size(); i++) {
            Edge cur = from.edges.get(i);
            Vertex cv = cur.other(from);
            if (cv == from) continue;

            if (cv.fmark < bestMark) {
                bestMark = cv.fmark;
                bestE = cur;
                bestV = cv;
            }
        }

        edges.add(bestE);

        walk(bestV, to, edges, vertices);
    }

    private static void shortestPath(
            Vertex start, Vertex end,
            List<Edge> edges, List<Vertex> vertices,
            int imark) {
        mark(end, 0, imark);
        walk(start, end, edges, vertices);
    }

    public static List<List<Vector2>> break_(List<Vector2> shape) {
        Vector2 center = new Vector2();
        List<Edge> edges = new ArrayList<Edge>();
        Queue<BRay> rays = new Queue<BRay>();

        {
            Vertex[] vertices = new Vertex[shape.size()];
            for (int i = 0; i < shape.size(); i++) {
                vertices[i] = new Vertex(shape.get(i));
                center = center.add(shape.get(i));
            }

            center.scl(1f / (float) vertices.length);

            for (int i = 0; i < shape.size(); i++) {
                edges.add(new Edge(vertices[i], vertices[(i + 1) % shape.size()], 1));
            }
        }

        Vertex centerVert = new Vertex(center);
//        rays.addLast(new BRay(
//                new Vector2(1,0).rotate(MathUtils.random(360f)),
//                centerVert, 50, null));
//        rays.addLast(new BRay(
//                new Vector2(1,0).rotate(MathUtils.random(360f)),
//                centerVert, 50, null));

        startRays(rays, new Vector2(1, 0), centerVert, 180, 6, 60, null);

        while (rays.size != 0) {
            BRay ray = rays.removeFirst();

            Edge edge = null;
            float dst = ray.maxLen * 2;

            for (Edge edgex : edges) {
                if (edgex == ray.owner) continue;
                if (edgex.contains(ray.start)) continue;
                float t = ray.test(edgex);

                if (t < dst) {
                    dst = t;
                    edge = edgex;
                }
            }

            if (null == edge) {
                edges.add(edge = new Edge(
                        ray.start,
                        new Vertex(ray.skip(ray.maxLen)),
                        2
                ));

//                rays.addLast(new BRay(
//                        new Vector2(ray.dir)
//                            .rotate(MathUtils.random(-45f, 45f)),
//                        edge.vertex2,
//                        ray.maxLen,
//                        edge
//                ));
//                rays.addLast(new BRay(
//                        new Vector2(ray.dir)
//                            .rotate(MathUtils.random(-45f, 45f)),
//                        edge.vertex2,
//                        ray.maxLen,
//                        edge
//                ));
                startRays(
                        rays,
                        ray.dir,
                        edge.vertex2,
                        80,
                        2,
                        ray.maxLen,
                        edge
                );

                continue;
            }

            edges.remove(edge);
            edge.dispose();

            Vertex third = new Vertex(ray.skip(dst));

            edges.add(new Edge(ray.start, third, 2));
            edges.add(new Edge(edge.vertex1, third, edge.order));
            edges.add(new Edge(edge.vertex2, third, edge.order));
        }

        List<List<Vector2>> res = new ArrayList<List<Vector2>>();

//        for (Edge edge : edges) {
//            if (edge.vertex2.pos.dst2(edge.vertex1.pos) < 0.1) continue;
//            Vector2 third = new Vector2(edge.vertex1.pos).add(edge.vertex2.pos).scl(.5f);
//            Vector2 d = new Vector2(edge.vertex1.pos).sub(edge.vertex2.pos).rotate(90).nor().scl(5);
//            third.add(d);
//            res.add(Arrays.asList(edge.vertex1.pos, edge.vertex2.pos, third));
//        }

        ArrayList<Edge> xEdges = new ArrayList<Edge>();
        ArrayList<Vertex> xVertices = new ArrayList<Vertex>();
        int i = 213;
        while (edges.size() != 0) {
            xEdges.clear();
            xVertices.clear();

            try {
//                Vertex corner = corner(edges);
//                Edge exe = corner.edges.get(0);
//                Vertex other = exe.other(corner);
//                edges.remove(exe);
//                exe.dispose();

                Edge exe = side(edges);
                Vertex corner = exe.vertex1;
                Vertex other = exe.vertex2;
                edges.remove(exe);
                exe.dispose();

                shortestPath(corner, other, xEdges, xVertices, ++i);

//                if (xVertices.size() <= 8) {
                    List<Vector2> resShape = new ArrayList<Vector2>(xVertices.size());
                    for (int j = 0; j < xVertices.size(); j++) {
                        resShape.add(xVertices.get(j).pos);
                    }
                    res.add(resShape);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

            for (int j = 0; j < xEdges.size(); j++) {
                Edge edge = xEdges.get(j);

                if (0 >= --edge.order) {
                    edges.remove(edge);
                    edge.dispose();
                }
            }
        }

        return res;
    }
}
