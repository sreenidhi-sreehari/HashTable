import java.util.Scanner;
class Plagiarism{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String a=sc.nextLine();
        String b=sc.nextLine();
        String[] w1=a.split(" ");
        String[] w2=b.split(" ");
        int match=0;
        for(int i=0;i<w1.length;i++){
            for(int j=0;j<w2.length;j++){
                if(w1[i].equals(w2[j])){
                    match++;
                    break;
                }
            }
        }
        double sim=(double)match/w1.length*100;
        System.out.println(sim);
    }
}