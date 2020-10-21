package com.simple.toolpackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FunctionTool {
    public static void main(String[] args) {
        ToolAggregation ta = new CommonAggregation();
        ta.add("test 1");
        ta.add("test 2");
        IIterator iterator = ta.getIterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
    /**
     * 功能聚合
     */
    public interface ToolAggregation {
        void add(Object obj);

        void remove(Object obj);

        IIterator getIterator();
    }

    /**
     * 功能聚合具体实现类
     */
    static class CommonAggregation implements ToolAggregation {

        List<Object> list = new ArrayList<>();

        @Override
        public void add(Object obj) {
            list.add(obj);
        }

        @Override
        public void remove(Object obj) {
            list.remove(obj);
        }

        @Override
        public IIterator getIterator() {
            return new FunctionIterator(list);
        }
    }

    /**
     * 功能迭代
     */
    interface IIterator {
        Object first();

        Object next();

        boolean hasNext();
    }

    /**
     * 功能迭代具体实现类
     */
    public static class FunctionIterator implements IIterator {

        List<Object> list;
        Iterator iterator;

        public FunctionIterator(List<Object> list) {
            this.list = list;
            this.iterator = list.iterator();
        }

        @Override
        public Object first() {
            return list.get(0);
        }

        @Override
        public Object next() {
            return iterator.next();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    }
}
