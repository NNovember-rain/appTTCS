package vn.mrlongg71.ps09103_assignment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.library.ShowRssActivity;
import vn.mrlongg71.ps09103_assignment.library.XMLParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RSSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RSSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSSFragment extends Fragment {

    ArrayList<String> arrayTitle = new ArrayList<>();
    ArrayList<String> arrLink = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rs, container, false);
        lv = view.findViewById(R.id.lv);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayTitle);
        new ReadRss().execute("https://www.nydailynews.com/cmlink/NYDN.News.rss");
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowRssActivity.class);
                intent.putExtra("linkNews", arrLink.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    public class ReadRss extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {

                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLParser xmlDomParser = new XMLParser();
            // chứa toàn bộ nội dung rss
            Document document = xmlDomParser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item"); // đọc từng thẻ

            String title = "";


            for (int i = 0; i < nodeList.getLength(); i++) {//nodeList.getLength() láy tất cả toàn bộ trong thẻ item
                Element element = (Element) nodeList.item(i);
                title = xmlDomParser.getValue(element, "title"); // đọc tiêu đề
                arrayTitle.add(title);
                arrLink.add(xmlDomParser.getValue(element, "link"));
            }

            adapter.notifyDataSetChanged();

        }
    }

}
