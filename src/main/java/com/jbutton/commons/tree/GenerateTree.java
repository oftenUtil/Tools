package main.java.com.jbutton.commons.tree;

import main.java.com.jbutton.commons.tree.model.FrontTreeNode;
import main.java.com.jbutton.commons.tree.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将数据具有树形结构的List，转为树
 * @author lee
 */
public class GenerateTree {

    /**
     * @param allNodes 存放树所有节点的list
     * @param rootNodeId 根节点的id，可以有多个根节点，根节点id可以为null或者其他字符串
     * @return
     */
    public static List<TreeNode> listToTree(List<TreeNode> allNodes, String rootNodeId){
        /**
         * 存放所有根节点
         */
        ArrayList<TreeNode> rootNodes = new ArrayList<>();
        /**
         * 存放所有非根节点
         */
        ArrayList<TreeNode> childNodes = new ArrayList<>();
        if(allNodes == null){
            throw new NullPointerException("没有树节点");
        }
        //查找所有根节点，将根节点和非根节点分开
        for(TreeNode treeNode: allNodes){
            if(rootNodeId == null){
                if(treeNode.getpId() == rootNodeId){
                    rootNodes.add(treeNode);
                }else {
                    childNodes.add(treeNode);
                }
            }else if(rootNodeId.equals(treeNode.getpId())){
                rootNodes.add(treeNode);
            }else {
                childNodes.add(treeNode);
            }
        }

        //遍历所有根节点，递归获取所有的子节点
        for(TreeNode treeNode : rootNodes){
            List<TreeNode> treeChildNodes = getChildNodes(childNodes, treeNode.getId());
            treeNode.setChildren(treeChildNodes);
        }

        return rootNodes;

    }

    private static List<TreeNode> getChildNodes(ArrayList<TreeNode> allNodes, String id) {
        ArrayList<TreeNode> parendNodes = new ArrayList<>();
        ArrayList<TreeNode> childNodes = new ArrayList<>();

        //找出父节点
        for(TreeNode treeNode : allNodes){
            if(id.equals(treeNode.getpId())){
                parendNodes.add(treeNode);
            }else {
                childNodes.add(treeNode);
            }
        }
        //递归找出子节点
        if(parendNodes.size() > 0){
            for(TreeNode treeNode : parendNodes){
                List<TreeNode> childNodes1 = getChildNodes(childNodes, treeNode.getId());
                treeNode.setChildren(childNodes1);
            }
        }

        return parendNodes;
    }

    /**
     * 将上面listToTree方法转换得到的树，每个节点加上level层次
     * @param allNode 树
     * @param rootLevel 根节点的层次，一般为1
     */
    public static void listToFrontTree(List<TreeNode> allNode, Integer rootLevel){

        for(TreeNode treeNode : allNode){
            HashMap<String, Integer> map = new HashMap<>();
            map.put(treeNode.getId(), rootLevel);
            setAllNodeLevel(treeNode, map);
            FrontTreeNode frontTreeNode = (FrontTreeNode) treeNode;
            frontTreeNode.setLevel(rootLevel);
        }
    }

    private static void setAllNodeLevel(TreeNode treeNode, Map<String, Integer> map){
        List<? extends TreeNode> children = treeNode.getChildren();
        if(children.size() == 0){
            return;
        }else {
            for(TreeNode node : children){
                Integer parentLevel = map.get(treeNode.getId());
                FrontTreeNode frontTreeNode = (FrontTreeNode) node;
                frontTreeNode.setLevel(parentLevel + 1);
                map.put(frontTreeNode.getId(), parentLevel + 1);
                setAllNodeLevel(frontTreeNode, map);
            }
        }
    }
}
