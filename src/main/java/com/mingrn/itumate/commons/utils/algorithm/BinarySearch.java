package com.mingrn.itumate.commons.utils.algorithm;

/**
 * 二分查找算法(也称为二分搜索算法)是一种在有序数组中查找某一特定元素的搜索算法。
 * 搜索过程从数组的中间元素开始,如果中间元素正好是要查找的元素,则搜索过程结束;
 * 如果某一特定元素大于或者小于中间元素,则在数组大于或小于中间元素的那一半中查找,
 * 而且跟开始一样从中间元素开始比较。如果在某一步骤数组为空,则代表找不到。这种搜索
 * 算法每一次比较都使搜索范围缩小一半。
 * <p>
 * 二分搜索在情况下的复杂度是对数时间,进行 <pre>O(log n)</pre> 次比较操作(n在此处
 * 是数组的元素数量,O是大O记号, log 是对数)。二分搜索使用常数空间,无论对任何大小的
 * 输入数据,算法使用的空间都是一样的。除非输入数据数量很少,否则二分搜索比线性搜索更快,
 * 但数组必须事先被排序。
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-06-23 20:43
 */
public class BinarySearch {

    private BinarySearch(){}

    /**
     * 二分查找算法,递归
     *
     * @param arr    数组
     * @param target 目标对象
     * @return 目标对象下标, 没有时返回 -1
     */
    public static int binarySearch(int[] arr, int target) {
        return binarySearch(arr, target, 0, arr.length - 1);
    }


    /**
     * 二分查找算法,递归
     *
     * @param arr    数组
     * @param target 目标对象
     * @param start  开始下标
     * @param end    结束下标
     * @return 目标对象下标, 没有时返回 -1
     */
    public static int binarySearch(int[] arr, int target, int start, int end) {

        if (start > end) {
            return -1;
        }

        // 插值查找
        /*int mid = start + (end - start) * (target - arr[start]) / (arr[end] - arr[start]);*/
        //二分查找
        int mid = start + (end - start) / 2;
        if (arr[mid] > target) {
            return binarySearch(arr, target, start, mid - 1);
        }
        if (arr[mid] < target) {
            return binarySearch(arr, target, mid + 1, end);
        }

        return mid;
    }

    /**
     * 二分查找算法,while 循环
     */
    /*public static int binarySearch(int[] arr, int target, int start, int end) {
        int result = -1;

        while (start <= end) {
            int mid = start + (end - start) / 2;    //防止溢位
            if (arr[mid] > target)
                end = mid - 1;
            else if (arr[mid] < target)
                start = mid + 1;
            else {
                result = mid;
                break;
            }
        }

        return result;
    }*/
}