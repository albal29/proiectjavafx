package Networking;

import domain.Friendship;

import java.util.ArrayList;
import java.util.List;

public class Networking {
    private final Iterable<Friendship> friendships;
    private final List<Long> nodes;
    private final Long size;
    private final Long[][] friendMatrix;

    public Networking(Iterable<Friendship> friendships, List<Long> nodes, Long size) {
        this.friendships = friendships;
        this.nodes = nodes;
        this.size = size;
        this.friendMatrix = new Long[105][105];
        for (int i = 0; i < 105; i++)
            for (int j = 0; j < 105; j++)
                friendMatrix[i][j] = 0L;
    }

    public void setFriendMatrix() {

        for (Friendship friendship : friendships) {
            friendMatrix[Math.toIntExact(friendship.getId().getLeft())][Math.toIntExact(friendship.getId().getRight())] = 1L;
            friendMatrix[Math.toIntExact(friendship.getId().getRight())][Math.toIntExact(friendship.getId().getLeft())] = 1L;
        }

    }

    public void DFS(boolean[] visited, int node) {
        visited[node] = true;
        for (Long nodeCurent : nodes) {
            if (friendMatrix[node][Math.toIntExact(nodeCurent)] == 1L && !visited[Math.toIntExact(nodeCurent)])
                DFS(visited, Math.toIntExact(nodeCurent));
        }
    }

    public void DFS2(boolean[] visited, int node, List<Long> intermidiate) {
        visited[node] = true;
        for (Long nodeCurent : nodes) {
            if (friendMatrix[node][Math.toIntExact(nodeCurent)] == 1L && !visited[Math.toIntExact(nodeCurent)]) {
                intermidiate.add(nodeCurent);
                DFS2(visited, Math.toIntExact(nodeCurent), intermidiate);
            }
        }
    }

    public int numberOfCommunities() {
        boolean[] visited = new boolean[Math.toIntExact(this.size)];
        for (Long node : nodes)
            visited[Math.toIntExact(node)] = false;
        int res = 0;
        for (Long node : nodes)
            if (!visited[Math.toIntExact(node)]) {
                res++;
                DFS(visited, Math.toIntExact(node));
            }
        return res;
    }

    public List<Long> mostSociableCommunity() {
        boolean[] visited = new boolean[Math.toIntExact(this.size)];
        List<Long> rez = new ArrayList<>();
        for (Long node : nodes)
            visited[Math.toIntExact(node)] = false;
        int Max = 0;
        for (Long node : nodes)
            if (!visited[Math.toIntExact(node)]) {
                List<Long> intermediate = new ArrayList<>();
                intermediate.add(node);
                DFS2(visited, Math.toIntExact(node), intermediate);
                if (Max < intermediate.size()) {
                    Max = intermediate.size();
                    rez = List.copyOf(intermediate);
                }
            }
        return rez;
    }

}
