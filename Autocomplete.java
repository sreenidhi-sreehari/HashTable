import java.util.Scanner;
class Autocomplete{
    static String[] words=new String[100];
    static int size=0;
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++)
            words[size++]=sc.next();
        String pre=sc.next();
        for(int i=0;i<size;i++){
            if(words[i].startsWith(pre))
                System.out.println(words[i]);
        }
    }
}