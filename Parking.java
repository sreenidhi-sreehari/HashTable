import java.util.Scanner;
class Parking{
    static String[] spot=new String[500];
    static int hash(String plate){
        int sum=0;
        for(int i=0;i<plate.length();i++)
            sum+=plate.charAt(i);
        return sum%500;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            String plate=sc.next();
            int h=hash(plate);
            while(spot[h]!=null)
                h=(h+1)%500;
            spot[h]=plate;
            System.out.println(h);
        }
    }
}