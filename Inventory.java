import java.util.Scanner;
class Inventory{
    static String[] product=new String[50];
    static int[] stock=new int[50];
    static int p=0;
    static int find(String id){
        for(int i=0;i<p;i++){
            if(product[i].equals(id))
                return i;
        }
        return -1;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            product[i]=sc.next();
            stock[i]=sc.nextInt();
            p++;
        }
        int q=sc.nextInt();
        for(int i=0;i<q;i++){
            String id=sc.next();
            int idx=find(id);
            if(idx!=-1 && stock[idx]>0){
                stock[idx]--;
                System.out.println("Success "+stock[idx]);
            }
            else
                System.out.println("OutOfStock");
        }
    }
}