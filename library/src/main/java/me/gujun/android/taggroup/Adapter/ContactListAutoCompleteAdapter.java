package me.gujun.android.taggroup.Adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.gujun.android.taggroup.ContactData;
import me.gujun.android.taggroup.R;
import me.gujun.android.taggroup.TagGroup;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by andy on 7/4/15.
 */
public class ContactListAutoCompleteAdapter extends ArrayAdapter<ContactData> {

    public ArrayList<ContactData> contactDataMainList = new ArrayList<>();
    public TagGroup tagGroup;

    public ContactListAutoCompleteAdapter(Context context, int resource, TagGroup tagGroup) {
        super(context, resource);
        this.tagGroup = tagGroup;
    }

    @Override
    public void add(ContactData contactData) {
        if (contactDataMainList.contains(contactData)) {
            contactDataMainList.remove(contactData);
        }
        contactDataMainList.add(contactData);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        contactDataMainList.clear();
        for (int i = 0; i < contactDataMainList.size(); i++) {
            contactDataMainList.remove(i);
        }
        contactDataMainList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (contactDataMainList != null && contactDataMainList.size() > 0)
            return contactDataMainList.size();
        else
            return 0;
    }

    @Override
    public ContactData getItem(int position) {
        return contactDataMainList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null || v.getTag() == null) {
            LayoutInflater vi = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.auto_complete_contact_data_item_row, null, false);
        }
        TextView userName = (TextView) v.findViewById(R.id.userName);
        TextView emailID = (TextView) v.findViewById(R.id.emailID);
        ImageView coverImage = (ImageView) v.findViewById(R.id.coverImage);
        if (contactDataMainList != null && contactDataMainList.size() > 0) {
            ContactData o = contactDataMainList.get(position);
            if (o != null) {
                userName.setText(o.getName());
                emailID.setText(o.getEmailID());
                if (o.getImageUri() != null)
                    coverImage.setImageURI(Uri.parse(o.getImageUri()));
            }
        }
        return v;
    }


    public Observable<ContactData> readContacts(final String str) {

        return Observable.create(new rx.Observable.OnSubscribe<ContactData>() {
            @Override
            public void call(Subscriber<? super ContactData> subscriber) {
                Cursor cur = null;
                try {
                    ContentResolver cr = getContext().getContentResolver();
                    String[] PROJECTION = new String[]{
                            ContactsContract.RawContacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_ID,
                            ContactsContract.CommonDataKinds.Email.DATA,
                            ContactsContract.CommonDataKinds.Photo.CONTACT_ID
                    };
                    String filter = ContactsContract.CommonDataKinds.Email.DATA + " != ''" + " AND " + ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
                    String order = ContactsContract.Contacts.DISPLAY_NAME + " LIMIT 100";
                    String selection = "%" + str + "%";
                    String[] selectionArgs = {selection};
                    cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, selectionArgs, order);
                    if (cur != null && cur.moveToFirst()) {
                        do {
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String photoId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                            String emailAddr = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            if (isValidEmail(emailAddr)) {
                                String photoUri = null;
                                if (photoId != null)
                                    photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photoId)).toString();
                                ContactData contactData = new ContactData(name, emailAddr, photoUri);
                                subscriber.onNext(contactData);
                            }
                        } while (cur.moveToNext());
                    }
                    if (cur != null) cur.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                    if (cur != null) cur.close();
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        try {
            return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public void addContact(ContactData contactData) {
        // TODO: refactor TagGroup for an add method
        List<String> emails = new ArrayList<>(Arrays.asList(tagGroup.getTags()));
        if (!emails.contains(contactData.getEmailID())) {
            emails.add(contactData.getEmailID());
            tagGroup.setTags(emails);
        }
    }
}
