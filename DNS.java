import java.util.Scanner;
class DNS{
    static String[] domain=new String[50];
    static String[] ip=new String[50];
    static long[] expiry=new long[50];
    static int size=0;
    static int find(String d){
        for(int i=0;i<size;i++){
            if(domain[i].equals(d))
                return i;
        }
        return -1;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            String d=sc.next();
            String address=sc.next();
            int ttl=sc.nextInt();
            domain[size]=d;
            ip[size]=address;
            expiry[size]=System.currentTimeMillis()+ttl*1000;
            size++;
        }
        String query=sc.next();
        int idx=find(query);
        if(idx!=-1 && System.currentTimeMillis()<expiry[idx])
            System.out.println(ip[idx]);
        else
            System.out.println("MISS");
    }
}