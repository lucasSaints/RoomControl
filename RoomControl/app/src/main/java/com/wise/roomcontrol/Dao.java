package com.wise.roomcontrol;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.wise.roomcontrol.activities.MainActivity;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Reuniao;
import com.wise.roomcontrol.classes.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dao {
    static public List<User> users = new ArrayList<>();
    static public List<Empresa> empresas = new ArrayList<>();
    //static public List<Reuniao> reunioes = new ArrayList<>();
    static public User logado;
    static final public String urlWS = "http://172.30.248.111:8080/ReservaDeSala/rest/";

    public String ServerInOutput(boolean in, String urlEnd, String[] variable, Object[] value) throws Exception{
        try{
            JSONObject json = new JSONObject();
            String jsonEncoded="kyugasdykgufsdgkutsdagfiu";
            if(in && variable.length>value.length) {
                for(int i=0;i<value.length;i++){
                    json.put(variable[i], value[i]);
                }
                System.out.println("\n\n\n" + json.toString());
                jsonEncoded = Base64.encodeToString(json.toString().getBytes("UTF-8"), Base64.NO_WRAP);

                Log.i("teste", "ServerInOutput: "+jsonEncoded);
            }

            URL url = new URL(urlWS+urlEnd);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(in)
                conn.setRequestMethod("POST");
            else
                conn.setRequestMethod("GET");
            if(in)
                conn.setDoOutput(true);
            conn.setRequestProperty("authorization", "secret");
            if(!in || !(variable.length>value.length)){
                for(int i=0;i<value.length;i++){
                    conn.setRequestProperty(variable[i], String.valueOf(value[i]));
                }
            }else{
                conn.setRequestProperty(variable[variable.length-1], jsonEncoded);
            }
            conn.setConnectTimeout(7000);
            conn.connect();

            return getResultString(conn);

        }catch(Exception e){
            e.printStackTrace();
            return "400";
        }
    }

    public String getResultString(HttpURLConnection conn) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            result.append(line);
        }
        rd.close();
        Log.i("teste", "ServerInOutput: "+result.toString());
        return result.toString();
    }

    public boolean validaLogin(String email, String senha){
        if(users!=null) {
            Log.i("teste", "validaLogin: users existe");
//            Log.i("teste", "validaLogin: user1"+users.get(0).getMail());
        }
        if(empresas!=null) {
            Log.i("teste", "validaLogin: empresas existe");
            //Log.i("teste", "validaLogin: empresa1"+empresas.get(0).getNome());
        }
        /*for (User i:users) {
            Log.i("teste", "email: "+i.getMail());
            if(i.getMail().equals(email)) {
                if (i.getSenha().equals(senha)){
                    Log.i("teste", "validaLogin: deu certo");
                    logado=i;
                    return true;
                }else
                    Log.i("teste", "validaLogin: senha incorreta");
                    //Toast.makeText(LoginActivity.class,R.string.senha_incorreta,Toast.LENGTH_LONG);
            }
                //Toast.makeText(LoginActivity.class,R.string.invalid_email,Toast.LENGTH_LONG);
        }*/
        String returnLogin="0";
        try {
            returnLogin = makeAuthRequest(email,senha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(returnLogin.contains("Login efetuado com sucesso!")) {
            return true;
        }
        Log.i("teste", "validaLogin: email não cadastrado");
        return false;
    }

    public String validaCadastro(String email, String senha, String user, int idOrg){
        /*String dominio="kkkkk";
        boolean deu=false;
        int aux = email.length();
        Log.i("teste", "validaCadastro: entrou");
        for (int i = 0; i < aux; i++) {
            if(email.charAt(i)=='@'&&aux>i+2) {
                dominio=email.substring(i+1);
                Log.i("teste", "validaCadastro: "+dominio);
                deu=true;
            }
        }
        if(deu) {
            Log.i("teste", "validaCadastro: entrou2");
            if (!dominio.equals("gmail.com") && !dominio.equals("hotmail.com") && !dominio.equals("yahoo.com.br") && !dominio.equals("outlook.com")) {
                Log.i("teste", "validaCadastro: entrou3");
                if(empresas.size()>0) {
                    for (Empresa i : empresas) {
                        Log.i("teste", "validaCadastro: " + dominio + " vs. " + i.getDominio());
                        if (dominio.equals(i.getDominio())) {
                            User usuario = new User(email, senha, user);
                            usuario.setEmpresaById(i.getId());
                            return true;
                        }
                    }
                }
                return false;
            }else {
                return false;
            }
        }else{
            Log.i("teste", "validaCadastro: arroba não encontrado");
            return false;
        }

        /*StringBuilder result = new StringBuilder();
        URL url = new URL(urlWS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         String urlParameters  = "param1=a&param2=b&param3=c";
         byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
         conn.setDoOutput( true );
         conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod("POST");
        conn.setRequestProperty("authorization", "secret");
        conn.setRequestProperty("email", email);
        conn.setRequestProperty("password", senha);
        conn.setRequestProperty("nome", user);
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
            return true;
        }*/
        /*String[] valores={email,senha,user}, variaveis={"email","senha","nome"};
        try {
            return ServerInOutput(true, "usuario/cadastro", variaveis, valores).equals("Usuário criado com sucesso");
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }*/
        try {
            StringBuilder result = new StringBuilder();
            JSONObject userJson = new JSONObject();
            userJson.put("email", email);
            userJson.put("senha", senha);
            userJson.put("nome", user);
            userJson.put("idOrganizacao", idOrg);
            System.out.println("\n\n\n" + userJson.toString());
            String userEncoded = Base64.encodeToString(userJson.toString().getBytes("UTF-8"),Base64.NO_WRAP);
            System.out.println(userEncoded);
            URL url = new URL(urlWS+"usuario/cadastro/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("authorization", "secret");
            conn.setRequestProperty("novoUsuario", userEncoded);
            conn.setDoOutput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            System.out.println(result.toString());
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "Exception";
        }
    }

    public static String makeAuthRequest(String email, String password) throws Exception {
        /*try {
            String wsURL = "http://172.30.248.111:8080/ReservaDeSala/rest/usuario/login";
            URL obj = new URL(wsURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("authorization", "secret");
            con.setConnectTimeout(2000);
            con.setRequestProperty("email", email);
            con.setRequestProperty("password", password);

            int responseCode = con.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 400;
        }*/

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlWS+"usuario/login/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", "secret");
            conn.setRequestProperty("email", email);
            conn.setRequestProperty("password", password);
            conn.setConnectTimeout(5000);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            System.out.println(email+" "+password);
            System.out.println(result.toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "400";
    }

    public static void playTickSound(Context ctx) {
        try {
            MediaPlayer mp = MediaPlayer.create(ctx, R.raw.selec31);
            mp.setLooping(false);
            mp.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
