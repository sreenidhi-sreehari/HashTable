import java.util.Scanner;
class UsernameChecker{
    static String[] users = new String[100];
    static int[] freq = new int[100];
    static int count = 0;
    static boolean check(String name){
        for(int i=0;i<count;i++){
            if(users[i].equals(name)){
                freq[i]++;
                return false;
            }
        }
        users[count]=name;
        freq[count]=1;
        count++;
        return true;
    }
    static void suggest(String name){
        for(int i=1;i<=3;i++){
            System.out.println(name+i);
        }
    }
    static void popular(){
        int max=0,idx=0;
        for(int i=0;i<count;i++){
            if(freq[i]>max){
                max=freq[i];
                idx=i;
            }
        }
        System.out.println(users[idx]+" "+max);
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            String u=sc.next();
            if(check(u))
                System.out.println("Available");
            else{
                System.out.println("Taken");
                suggest(u);
            }
        }
        popular();
    }
}