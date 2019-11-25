package main.java.com.jbutton.commons.tree.model;

/**
 * 如果树是前端使用，并且需要一些额外字段，可以在本类中加
 * @author lee
 */
public class FrontTreeNode extends TreeNode{
    /**
     * 节点的key
     */
    private String key;
    /**
     * 节点的层次
     */
    private Integer level;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
