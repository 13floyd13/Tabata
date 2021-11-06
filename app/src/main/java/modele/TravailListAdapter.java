package modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.tabata.R;


import java.util.List;

public class TravailListAdapter extends ArrayAdapter<Travail> {


    public TravailListAdapter(Context mCtx, List<Travail> objects) {
        super(mCtx, R.layout.adapter_view_layout_travail, objects);
    }


    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        // récupération du travail
        final Travail travail = getItem(position);

        //on charge le xml qu'on va inflate
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_view_layout_travail, parent, false);


        //récupération des objets graphiques
        TextView textViewNom = (TextView) rowView.findViewById(R.id.adapter_nomTravail);
        TextView textViewTempsTravail = (TextView) rowView.findViewById(R.id.adapter_tempsTravail);
        TextView textViewTempsRepos = (TextView) rowView.findViewById(R.id.adapter_tempsRepos);

        //récupération des Strings en ressources
        String strTravail = getContext().getString(R.string.travail);
        String strSecondes = getContext().getString(R.string.secondes);
        String strRepos = getContext().getString(R.string.repos);
        String space = " ";

        //on remplit les text view
        textViewNom.setText(travail.getNom());
        textViewTempsTravail.setText(strTravail + space + travail.getTemps() + space + strSecondes);
        textViewTempsRepos.setText(strRepos + space + travail.getRepos() + space + strSecondes);

        return rowView;




    }
}
