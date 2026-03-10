import java.util.Scanner;
class Cache{
    static String[] L1=new String[10];
    static String[] L2=new String[50];
    static boolean find(String[] arr,String v){
        for(int i=0;i<arr.length;i++){
            if(arr[i]!=null && arr[i].equals(v))
                return true;
        }
        return false;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            String video=sc.next();
            if(find(L1,video))
                System.out.println("L1 HIT");
            else if(find(L2,video))
                System.out.println("L2 HIT");
            else
                System.out.println("DB HIT");
        }
    }