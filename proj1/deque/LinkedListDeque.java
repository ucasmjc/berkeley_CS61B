package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>,Deque<T> {
    public class IntNode {
        public IntNode last;
        public IntNode next;
        public T item;

        public IntNode(T get, IntNode gnext,IntNode glast) {
            item = get;
            next = gnext;
            last=glast;
        }
    }
    public IntNode sentinel;
    private int size;
    public int size(){
        return size;
    }
    public LinkedListDeque(){
        sentinel=new IntNode(null,null,null);
        sentinel.next=sentinel;
        sentinel.last=sentinel;
        size=0;
    }
    public LinkedListDeque(T x){
        sentinel=new IntNode(null,null,null);
        sentinel.next=new IntNode(x,sentinel,sentinel);
        sentinel.last=sentinel.next;
        size=1;
    }
    @Override
    public void addFirst(T x){
        sentinel.next=new IntNode(x,sentinel.next,sentinel);
        sentinel.next.next.last=sentinel.next;
        size+=1;
    }
    @Override
    public void addLast(T x){
        sentinel.last.next=new IntNode(x,sentinel,sentinel.last);
        sentinel.last=sentinel.last.next;
        size+=1;
    }


    @Override
    public void printDeque(){
        IntNode p=sentinel.next;
        while(p!=sentinel){
            System.out.print(p.item);
            System.out.print(' ');
            p=p.next;
        }
        System.out.println();
    }
    @Override
    public T removeFirst(){
        if(isEmpty()){return null;}
        T i=sentinel.next.item;
        sentinel.next=sentinel.next.next;
        sentinel.next.last=sentinel;
        size-=1;
        return i;
    }
    @Override
    public T removeLast(){
        if(isEmpty()){return null;}
        T i =sentinel.last.item;
        sentinel.last.last.next=sentinel;
        sentinel.last=sentinel.last.last;
        size-=1;
        return i;
    }
    @Override
    public T get(int index){
        if(size<index+1){return null;}
        IntNode p=sentinel.next;
        while(index>0){
            p=p.next;
            index--;
        }
        return p.item;
    }

    public class LinkedIterator implements Iterator<T>{
        private int mark;
        private IntNode now;
        public LinkedIterator(){
            mark=0;
            now=sentinel;
        }
        public boolean hasNext(){
            return mark<size;
        }
        public T next(){
            now=now.next;
            mark+=1;
            return now.item;
        }
    }
    public Iterator<T> iterator(){
        return new LinkedIterator();
    }

    public boolean equals(Object o){
        if(o instanceof LinkedListDeque){
            LinkedListDeque p=(LinkedListDeque) o;
            if(p.size()!=size){return false;}
            if(size==0){
                if(p.size()==0){
                    return true;
                }
                return false;
            }
            IntNode q=p.sentinel.next;
            for(T item:this){
                if(!item.equals(q.item)){return false;}
                q=q.next;
            }
            return true;
        }
        return false;
    }
    public T getRecursive(int index){
        if(size<index+1){return null;}
        if(index==0){return this.sentinel.next.item;}
        LinkedListDeque<T> next=new LinkedListDeque<>();
        next.sentinel.last=this.sentinel.last;
        next.sentinel.next=this.sentinel.next.next;
        return next.getRecursive(index-1);
    }
}
