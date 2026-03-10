import java.util.Scanner;
class RateLimiter{
    static String[] client=new String[100];
    static int[] req=new int[100];
    static int size=0;
    static int find(String c){
        for(int i=0;i<size;i++){
            if(client[i].equals(c))
                return i;
        }
        return -1;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int limit=sc.nextInt();
        int q=sc.nextInt();
        for(int i=0;i<q;i++){
            String id=sc.next();
            int idx=find(id);
            if(idx==-1){
                client[size]=id;
                req[size]=1;
                size++;
                System.out.println("Allowed");
            }
            else{
                if(req[idx]<limit){
                    req[idx]++;
                    System.out.println("Allowed");
                }
                else
                    System.out.println("Denied");
            }
        }
    }
}