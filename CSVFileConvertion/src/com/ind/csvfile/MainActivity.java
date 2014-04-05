package com.ind.csvfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MainActivity extends Activity {
	EditText name, age, address;
	Spinner bloodgroup;
	Button save, Export, ExportPDF;
	TextView status;
	DBHelper dbHelper;
	BasicItems basicItems;
	String[] bgroups = { "Select bloodgroup", "A+", "A-", "B+", "B-", "AB+",
			"AB-", "O+", "O-" };
	ArrayAdapter<?> groupAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHelper = new DBHelper(getApplicationContext());
		ui();
		showStatus();
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String nameVal = name.getText().toString();
				String ageVal = age.getText().toString();
				String addrVal = address.getText().toString();
				String bgroupVal = bloodgroup.getSelectedItem().toString();
				if (nameVal.equals("") || nameVal.equals(null)
						&& ageVal.equals("") || nameVal.equals(null)
						&& addrVal.equals("") || addrVal.equals(null)
						&& bgroupVal.equals("Select bloodgroup")
						|| bgroupVal.equals(null)) {
					Toast.makeText(MainActivity.this,
							"Please enter the values", Toast.LENGTH_SHORT)
							.show();

				} else {
					dbHelper.insertValues(new BasicItems(nameVal, ageVal,
							addrVal, bgroupVal));
					clear();
					Intent refresh = getIntent();
					refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					startActivity(refresh);
				}
			}
		});
		if (Export.isEnabled()) {
			Export.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						new ExportCSVTask().execute("");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			ExportPDF.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						new ExportPDFTask().execute("");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}

	public void ui() {
		name = (EditText) findViewById(R.id.name);
		age = (EditText) findViewById(R.id.age);
		address = (EditText) findViewById(R.id.address);
		bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
		save = (Button) findViewById(R.id.save);
		Export = (Button) findViewById(R.id.export);
		ExportPDF = (Button) findViewById(R.id.pdf_export);
		status = (TextView) findViewById(R.id.status);
		groupAdapter = new ArrayAdapter<Object>(this,
				android.R.layout.simple_spinner_item, bgroups);
		bloodgroup.setAdapter(groupAdapter);
	}

	public void showStatus() {
		int count = dbHelper.getProfilesCount();
		if (count == 0) {
			status.setText("No more data, please enter some data");
			Export.setEnabled(false);
			ExportPDF.setEnabled(false);
		} else {
			status.setText("");
			Export.setEnabled(true);
			ExportPDF.setEnabled(true);
		}
	}

	public String getDateTime() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public void clear() {
		name.setText("");
		age.setText("");
		address.setText("");
		bloodgroup.setSelection(0);
	}

	public class ExportCSVTask extends AsyncTask<String, Void, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(
				MainActivity.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Exporting database...");
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			File dbFile = getDatabasePath("basic_db");
			File exportDB = new File(Environment.getExternalStorageDirectory(),
					"");
			if (!exportDB.exists()) {
				exportDB.mkdirs();
			}
			File file = new File(exportDB, getDateTime() + ".csv");
			try {
				file.createNewFile();
				CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				Cursor curCSV = db.rawQuery("SELECT * FROM basic_table", null);
				csvWriter.writeNext(curCSV.getColumnNames());
				while (curCSV.moveToNext()) {
					String arrStr[] = { curCSV.getString(0),
							curCSV.getString(1), curCSV.getString(2),
							curCSV.getString(3), curCSV.getString(4) };
					csvWriter.writeNext(arrStr);
				}
				csvWriter.close();
				curCSV.close();
				return true;
			} catch (SQLException sqlEx) {
				Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
				return false;
			} catch (IOException e) {
				Log.e("MainActivity", e.getMessage(), e);
				return false;
			}
		}

		protected void onPostExecute(final Boolean success) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (success) {
				Toast.makeText(MainActivity.this, "Export File successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Export failed",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/*
	 * Task of Creating PDF
	 */
	public class ExportPDFTask extends AsyncTask<String, Void, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(
				MainActivity.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Exporting database...");
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor c1 = db.rawQuery("SELECT * FROM basic_table", null);
			File dbFile = getDatabasePath("basic_db");
			File exportDB = new File(Environment.getExternalStorageDirectory(),
					"");
			Document document = new Document();
			if (!exportDB.exists()) {
				exportDB.mkdirs();
			}
			File file = new File(exportDB, getDateTime() + ".pdf");
			try {
				PdfWriter.getInstance(document, new FileOutputStream(file));
				document.open();
				Paragraph header = new Paragraph();
				header.add("PDF Export Example by using iTextPDF");
				PdfPTable pdfPTable = new PdfPTable(5);
				pdfPTable.addCell("ID");
				pdfPTable.addCell("NAME");
				pdfPTable.addCell("AGE");
				pdfPTable.addCell("ADDRESS");
				pdfPTable.addCell("BLOODGROUP");
				while (c1.moveToNext()) {
					String _id = c1.getString(0);
					String c_name = c1.getString(1);
					String c_age = c1.getString(2);
					String c_address = c1.getString(3);
					String c_bgroup = c1.getString(4);
					pdfPTable.addCell(_id);
					pdfPTable.addCell(c_name);
					pdfPTable.addCell(c_age);
					pdfPTable.addCell(c_address);
					pdfPTable.addCell(c_bgroup);
				}
				document.add(pdfPTable);
				document.addCreationDate();
				document.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		protected void onPostExecute(final Boolean success) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (success) {
				Toast.makeText(MainActivity.this, "Export File successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Export failed",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_info:
			Intent infoIntent = new Intent(MainActivity.this, DialogActivity.class);
			startActivity(infoIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
