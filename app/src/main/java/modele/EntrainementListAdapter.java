package modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tabata.R;

import java.util.List;

public class EntrainementListAdapter extends ArrayAdapter<EntrainementAvecSequences>{


    public EntrainementListAdapter(Context mCtx, List<EntrainementAvecSequences> objects) {
        super(mCtx, R.layout.adapter_view_layout_entrainement, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //récupération de l'entrainement
        final EntrainementAvecSequences entrainementAvecSequences = getItem(position);

        //on charge le xml à inflate
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_view_layout_entrainement, parent, false);

        //récupération des objets graphiques
        TextView textViewNom = (TextView) rowView.findViewById(R.id.adapter_nomEntrainement);
        TextView textViewDescription = (TextView) rowView.findViewById(R.id.adapter_descriptionEntrainement);
        TextView textViewTempsPreparation = (TextView) rowView.findViewById(R.id.adapter_tempsPreparation);
        TextView textViewTempsRepos = (TextView) rowView.findViewById(R.id.adapter_tempsRepos);
        TextView textViewNomsSequences = (TextView) rowView.findViewById(R.id.adapter_nomsSequences);

        //récupération des strings en ressources
        String strTemps = getContext().getString(R.string.temps);
        String strRepos = getContext().getString(R.string.repos);
        String strSecondes = getContext().getString(R.string.secondes);
        String strPreparation = getContext().getString(R.string.preparation);
        String space = " ";
        String strSequence= getContext().getString(R.string.sequence);

        //récupération des infos pour remplir les textView
        Entrainement entrainement = entrainementAvecSequences.getEntrainement();

        textViewNom.setText(entrainement.getNom());
        textViewDescription.setText(entrainement.getDescription());
        textViewTempsPreparation.setText(strTemps + space + strPreparation + space + entrainement.getTempsPreparation() + space + strSecondes);
        textViewTempsRepos.setText(strTemps + space + strRepos + space + entrainement.getTempsRepos() + space + strSecondes);

        //récupération des noms des sequences pour les afficher
        String strListeSequences = strSequence + "\n";

        for (int i = 0; i < entrainementAvecSequences.getSequences().size(); i++){

            strListeSequences += entrainementAvecSequences.getSeq().get(i).getNom();
            strListeSequences += space;
        }

        textViewNomsSequences.setText(strListeSequences);

        return rowView;
    }
}


