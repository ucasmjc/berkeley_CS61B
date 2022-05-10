package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>,Deque<T>{
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int capture;
    public T[] sorted;
    private int last;
    private int first;
    @Override
    public int size(){return size;}

    public ArrayDeque(){
        items=(T[])new Object[8];
        size=0;
        nextfirst=7;
        nextlast=0;
        capture=8;
    }

    public void mask(){
        if(nextlast==0){
            last=capture-1;
        }else{
            last=nextlast-1;
        }
        if(nextfirst==capture-1){
            first=0;
        }else{
            first=nextfirst+1;
        }
    }
    public void sort(){
        T[]a =(T[])new Object[capture];
        mask();
        if(size==0){return;}
        if(last>first){
            System.arraycopy(items,first,a,0,size);
        }else{
            System.arraycopy(items,first,a,0,capture-first);
            System.arraycopy(items,0,a,capture-first,last+1);
        }
        this.sorted=a;
    }
    public void trick(double x){
        int cap=(int)Math.round(x);
        T[] a=(T[])new Object[cap];
        sort();
        System.arraycopy(sorted,0,a,0,size);
        nextfirst=cap-1;
        nextlast=size;
        items=a;
        capture=cap;
    }
    public void resize(){
        if(nextfirst==nextlast){
           trick(capture*1.5);
        }
        if(size>=16){
            if(((nextfirst>nextlast)&&((nextfirst-nextlast+1)/size>0.7))||((nextfirst<nextlast)&&((nextlast-nextfirst)/size<0.3))){
                    trick(0.6*capture);
            }
        }
    }
    @Override
    public void addFirst(T x){
        items[nextfirst]=x;
        size++;
        if(nextfirst==0){
            nextfirst=capture-1;
        }else{
            nextfirst--;
        }
        resize();
    }
    @Override
    public void addLast(T x){
        items[nextlast]=x;
        size++;
        if(nextlast==capture-1){
            nextlast=0;
        }else {
            nextlast++;
        }
        resize();;
    }

    @Override
    public void printDeque(){
        if (size > 0) {
            mask();
            if(last>first){
                for(int i = first;i<=last;i++){
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
            }else {
                for(int i=first;i<capture;i++){
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
                for(int i=0;i<=last;i++){
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
            }

        }
        System.out.println();

    }
    @Override
    public T removeFirst(){
        if(size==0){return  null;}
       mask();
        nextfirst=first;
        T leo=items[first];
        items[first]=null;
        size--;
        resize();
        return leo;
    }
    @Override
    public T removeLast(){
        if(size==0){return  null;}
        mask();
        nextlast=last;
        T leo=items[last];
        items[last]=null;
        size--;
        resize();
        return leo;
    }
    @Override
    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        int ma = nextfirst + 1 + index;
        if (ma >= capture) {
            return items[ma-capture];
        }else{
            return items[ma];
        }
    }

    public class ArrayIterator implements Iterator<T>{
        private int mark;
        private T[] ite;
        public ArrayIterator(){
            mark=0;
            sort();
            ite=sorted;
        }
        public boolean hasNext(){
            return mark<size;
        }
        public T next(){
            mark+=1;
            return ite[mark-1];
        }
    }
    @Override
    public Iterator<T> iterator(){
        return new ArrayIterator();
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof ArrayDeque){
           ArrayDeque p=(ArrayDeque) o;
            if(p.size()!=size){return false;}
            if(size==0){
                return true;
            }
            p.sort();
            this.sort();
            for(int i=0;i<size;i++){
                if(!sorted[i].equals(p.sorted[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
