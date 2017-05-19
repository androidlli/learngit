package com.cango.palmcartreasure.model;

import java.util.List;

/**
 * Created by cango on 2017/5/17.
 */

public class TaskDetailData {
    List<TaskSection> taskSectionList;

    public List<TaskSection> getTaskSectionList() {
        return taskSectionList;
    }

    public void setTaskSectionList(List<TaskSection> taskSectionList) {
        this.taskSectionList = taskSectionList;
    }

    public static class TaskSection{
        public int getIvId() {
            return ivId;
        }

        public void setIvId(int ivId) {
            this.ivId = ivId;
        }

        int ivId;
        String title;
        List<TaskInfo> taskInfoList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<TaskInfo> getTaskInfoList() {
            return taskInfoList;
        }

        public void setTaskInfoList(List<TaskInfo> taskInfoList) {
            this.taskInfoList = taskInfoList;
        }

        public static class TaskInfo{
            String left;

            public int getLeftColor() {
                return leftColor;
            }

            public void setLeftColor(int leftColor) {
                this.leftColor = leftColor;
            }

            public int getCenterColor() {
                return centerColor;
            }

            public void setCenterColor(int centerColor) {
                this.centerColor = centerColor;
            }

            public int getRightColor() {
                return rightColor;
            }

            public void setRightColor(int rightColor) {
                this.rightColor = rightColor;
            }

            int leftColor;
            String center;
            int centerColor;
            String right;
            int rightColor;

            public String getLeft() {
                return left;
            }

            public void setLeft(String left) {
                this.left = left;
            }

            public String getCenter() {
                return center;
            }

            public void setCenter(String center) {
                this.center = center;
            }

            public String getRight() {
                return right;
            }

            public void setRight(String right) {
                this.right = right;
            }
        }
    }
}
