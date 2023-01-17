import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
       /*     OwnList<Integer> arr = new OwnList<>();
        System.out.println( arr.isEmpty());
            System.out.println( arr.add(1));
            System.out.println( arr.add(2));
            System.out.println( arr.size());
            System.out.println( arr.contains(1));
            System.out.println( arr.isEmpty());
            System.out.println(arr.toArray());
            System.out.println( arr.remove(Integer.valueOf(1)));
            System.out.println( arr.remove(0));
            System.out.println( arr.isEmpty());
             arr.add(0,1);
        System.out.println(arr.indexOf(1));
        System.out.println(arr.indexOf(999));
        arr.addAll(List.of(2,3,4,5));
        System.out.println(arr);
        arr.removeAll(List.of(2,3));
        System.out.println(arr);
        arr.retainAll(List.of(1,4));
        System.out.println(arr);
        System.out.println(arr.get(0));
        arr.addAll(List.of(1,2,3,4));
        System.out.println(arr.containsAll(List.of(1,2,3,4,5)));
            LRUCache newLru = new LRUCache(5);
            newLru.put(1,"a");
        newLru.put(2,"b");
        newLru.put(3,"c");
        newLru.put(4,"d");
        newLru.put(5,"e");
        newLru.put(6,"f");*/
        ModifiedList<Integer> list = new ModifiedList<>(2);
        System.out.println(list.isEmpty());
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list.isEmpty());
        System.out.println(list.size());
        System.out.println(list.contains(3));

        list.add(4);
        list.add(5);
        list.add(6);
        System.out.println(list.contains(6));
        //
        list.toArray();
        list.remove(Integer.valueOf(1));
        list.remove(Integer.valueOf(4));
        System.out.println(list.containsAll(List.of(1,3,5,6)));

        ModifiedList<Integer> list2 = new ModifiedList<>(4);
        list2.addAll(List.of(1,2,3,4,5,6,7));
        list2.removeAll(List.of(1,3,6));
        list2.retainAll(List.of(5));
        System.out.println( list2.get(0));
        System.out.println(list2.get(4));

    }





}
