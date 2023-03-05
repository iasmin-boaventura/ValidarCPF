package com.mycompany.lista2;
import java.util.Scanner;

public class ex4 {
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.printf("Digite o CPF: ");
        String cpfstr = sc.nextLine();
        if(cpfstr.length() == 11 || !"11111111111".equals(cpfstr) || !"22222222222".equals(cpfstr)
                || !"33333333333".equals(cpfstr)|| !"44444444444".equals(cpfstr)|| !"55555555555".equals(cpfstr)
                || !"66666666666".equals(cpfstr)|| !"77777777777".equals(cpfstr)|| !"88888888888".equals(cpfstr)
                || !"99999999999".equals(cpfstr)|| !"00000000000".equals(cpfstr));{
            //transforma o cpf recebido em string em um vetor int
            String[] numstr = cpfstr.split("");
            int cpf[] = new int[11];
            for(int i=0;i<11;i++){
                cpf[i] = Integer.parseInt(numstr[i]);
            }

            //digito j
            int j=0;
            int a=10;
            for(int i=0;i<9;i++){
                j = j + (cpf[i]*a);
                a--;
            }

            int modj = j%11;
            int digj=0;

            if(modj==0 || modj==1){
                digj=0;
            }else{
                if(modj>=2 && modj<=10)
                    digj=11-modj;
            }

            //digito k
            int k=0;
            a=11;
            for(int i=0;i<10;i++){
                k = k + (cpf[i]*a);
                a--;
            }

            int modk = k%11;
            int digk=0;

            if(modk==0 || modk==1){
                digk=0;
            }else{
                if(modk>=2 && modk<=10)
                    digk=11-modk;
            }

            //saída
            if(digj==cpf[9] && digk==cpf[10]){
                System.out.println("CPF válido");
            }else{
                System.out.println("CPF inválido");
            }
        }
    }
}
