import java.util.Scanner;
class Analytics{
    static String[] page=new String[100];
    static int[] count=new int[100];
    static int size=0;
    static int find(String p){
        for(int i=0;i<size;i++){
            if(page[i].equals(p))
                return i;
        }
        return -1;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            String p=sc.next();
            int idx=find(p);
            if(idx==-1){
                page[size]=p;
                count[size]=1;
                size++;
            }
            else
                count[idx]++;
        }
        for(int i=0;i<size;i++)
            System.out.println(page[i]+" "+count[i]);
    }
}