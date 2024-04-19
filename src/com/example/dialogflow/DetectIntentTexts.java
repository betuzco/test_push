/*
  Copyright 2017, Google, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.example.dialogflow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
// [START dialogflow_import_libraries]
// Imports the Google Cloud client library
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
// [END dialogflow_import_libraries]


/**
 * DialogFlow API Detect Intent sample with text inputs.
 */
public class DetectIntentTexts {
	
	static final String JSON_PATH = "C:\\Workspaces\\DialogFlow\\config\\agent-human-handoff-sample-bep-d053b573cda8.json";

  // [START dialogflow_detect_intent_text]
  /**
   * Returns the result of detect intent with texts as inputs.
   *
   * Using the same `session_id` between requests allows continuation of the conversation.
   * @param projectId Project/Agent Id.
   * @param texts The text intents to be detected based on what a user says.
   * @param sessionId Identifier of the DetectIntent session.
   * @param languageCode Language code of the query.
   */
  public static void detectIntentTexts(String projectId, List<String> texts, String sessionId,
      String languageCode) throws Exception {
    // Instantiates a client
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Detect intents for each text input
      for (String text : texts) {
        // Set the text (hello) and language code (en-US) for the query
        Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

        // Build the query with the TextInput
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        // Performs the detect intent request
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

        // Display the query result
        QueryResult queryResult = response.getQueryResult();

        System.out.println("====================");
        System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
        System.out.format("Detected Intent: %s (confidence: %f)\n",
            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
        System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
      }
    }
  }
  // [END dialogflow_detect_intent_text]
  
  static void authImplicit() {
	  // If you don't specify credentials when constructing the client, the client library will
	  // look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
	  Storage storage = StorageOptions.getDefaultInstance().getService();

	  System.out.println("Buckets:");
	  Page<Bucket> buckets = storage.list();
	  for (Bucket bucket : buckets.iterateAll()) {
	    System.out.println(bucket.toString());
	  }
	}
  
//  static void authExplicit() throws IOException {
//	  // You can specify a credential file by providing a path to GoogleCredentials.
//	  // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
//	  InputStream is = new FileInputStream(JSON_PATH);
//	  System.out.println("IS == " + is);
//	  GoogleCredentials credentials = GoogleCredentials.fromStream(is)
//	        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
//	  Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//
//	  System.out.println("Buckets:");
//	  Page<Bucket> buckets = storage.list();
//	  for (Bucket bucket : buckets.iterateAll()) {
//	    System.out.println(bucket.toString());
//	  }
//	}

  static void authExplicit(String jsonPath) throws IOException {
	  // You can specify a credential file by providing a path to GoogleCredentials.
	  // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
	  GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
	        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
	  Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

	  System.out.println("Buckets:");
	  Page<Bucket> buckets = storage.list();
	  for (Bucket bucket : buckets.iterateAll()) {
	    System.out.println(bucket.toString());
	  }
	  
//	  GoogleCredentials credentials = ComputeEngineCredentials.create();
//	  Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("agent-human-handoff-sample-bep").build().getService();
//	  System.out.println("Buckets:");
//	  Page<Bucket> buckets = storage.list();
//	  for (Bucket bucket : buckets.iterateAll()) {
//	   System.out.println(bucket.toString());
//	  }
	}
  
  // [START run_application]
  public static void main(String[] args) throws Exception {
    ArrayList<String> texts = new ArrayList<>();
    texts.add("hello");
    String projectId = "agent-human-handoff-sample-bep";
    String sessionId = UUID.randomUUID().toString();
    String languageCode = "en-US";
    String jsonPath = "C:\\oceana-df.json";
    authExplicit(JSON_PATH);
//    authExplicit("C:\\Users\\dperezrivas\\Documents\\Oceana Bot POC\\oceana-df-poc-rpvesc-18b263e82190.json");
    detectIntentTexts(projectId, texts, sessionId, languageCode);
  }
  // [END run_application]
}
