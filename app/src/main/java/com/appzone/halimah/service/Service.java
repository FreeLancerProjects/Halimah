package com.appzone.halimah.service;

import com.appzone.halimah.models.ContactModel;
import com.appzone.halimah.models.NotificationModel;
import com.appzone.halimah.models.NotificationReadModel;
import com.appzone.halimah.models.ReservationModel;
import com.appzone.halimah.models.ReserveModel;
import com.appzone.halimah.models.ResponseModel;
import com.appzone.halimah.models.ServiceModel;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.appzone.halimah.models.TermsModel;
import com.appzone.halimah.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Service {

    @GET("AboutApp/TermsAndConditions")
    Call<TermsModel> getTerms_Condition();

    @GET("AboutApp/SocialMedia")
    Call<ContactModel> getContactUs();

    @FormUrlEncoded
    @POST("AboutApp/ContactUs")
    Call<ResponseModel> sendContactUs(@Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("message") String message
    );

    @FormUrlEncoded
    @POST("AppUser/UpdateLocation/{user_id}")
    Call<ResponseModel> updateLocation(@Path("user_id")String user_id,
                                       @Field("user_google_lat") double user_google_lat,
                                       @Field("user_google_long") double user_google_long
    );

    @FormUrlEncoded
    @POST("AppUser/UpdateTokenId/{user_id}")
    Call<ResponseModel> updateToken(@Path("user_id")String user_id,
                                    @Field("user_token_id") String user_token_id
    );


    @GET("AppUser/Logout/{user_id}")
    Call<ResponseModel> logout(@Path("user_id") String user_id);
    @FormUrlEncoded
    @POST("AppUser/RestMyPass")
    Call<ResponseModel> resetPassword(@Field("user_email") String user_email,
                                      @Field("user_name") String user_name
                                      );
    @GET("Api/AllService")
    Call<List<ServiceModel>>  getAllServices();

    @FormUrlEncoded
    @POST("AppUser/Login")
    Call<UserModel> sign_in(@Field("user_name") String user_name,
                            @Field("user_pass") String user_pass
                            );

    @FormUrlEncoded
    @POST("AppUser/SignUp")
    Call<UserModel> sign_up_client(@Field("user_full_name") String user_full_name,
                                   @Field("user_name") String user_name,
                                   @Field("user_phone") String user_phone,
                                   @Field("user_pass") String user_pass,
                                   @Field("user_type") String user_type
                                   );

    @Multipart
    @POST("AppUser/SignUp")
    Call<UserModel> sign_up_Nursery(@Part("user_full_name") RequestBody user_full_name,
                                    @Part("user_name") RequestBody user_name,
                                    @Part("user_pass") RequestBody user_pass,
                                    @Part("user_phone") RequestBody user_phone,
                                    @Part("user_google_lat") RequestBody user_google_lat,
                                    @Part("user_google_long") RequestBody user_google_long,
                                    @Part("person_responsible_name") RequestBody person_responsible_name,
                                    @Part("from_hour") RequestBody from_hour,
                                    @Part("to_hour") RequestBody to_hour,
                                    @Part("user_type") RequestBody user_type,
                                    @Part("user_address") RequestBody user_address,
                                    @Part("hour_cost") RequestBody hour_cost,
                                    @Part("person_responsible_phone") RequestBody person_responsible_phone,
                                    @Part("kindergarten_service[]") List<RequestBody> kindergarten_service,
                                    @Part MultipartBody.Part user_image
                                    );


    @FormUrlEncoded
    @POST("Api/Nursery/{user_id}")
    Call<Slider_Nursery_Model> getSlider_Nursery(@Path("user_id") String user_id,
                                                 @Field("my_lat") double my_lat,
                                                 @Field("my_long") double my_long
                                                 );


    @POST("Api/Resevation/{user_id}/{nursery_id}/{cost}")
    Call<ResponseModel> reserve(@Path("user_id")String user_id,
                                @Path("nursery_id")String nursery_id,
                                @Path("cost")String cost,
                                @Body List<ReserveModel> responseModelList
                                );

    @GET("Api/MyAlert/{user_id}")
    Call<List<NotificationModel>> getClientNotifications(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/ReadAlerts")
    Call<NotificationReadModel> getUnreadClientNotificationCount(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("Api/ReadAlerts")
    Call<ResponseModel> readClientNotificationCount(@Field("user_id") String user_id,
                                                             @Field("read_all") String read_all
    );

    @GET("Api/NurseryAlert/{user_id}")
    Call<List<NotificationModel>> getNurseryNotifications(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/NurseryReadAlerts")
    Call<NotificationReadModel> getUnreadNurseryNotificationCount(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("Api/NurseryReadAlerts")
    Call<ResponseModel> readNurseryNotificationCount(@Field("user_id") String user_id,
                                                             @Field("read_all") String read_all
                                                             );


    @FormUrlEncoded
    @POST("Api/Confirm/{user_id}/{id_reservation}")
    Call<ResponseModel> nursery_accept_refuse_all_reservations(@Path("user_id")String user_id,
                                                        @Path("id_reservation")String id_reservation,
                                                        @Field("accepted_type") String accepted_type
    );
    @FormUrlEncoded
    @POST("Api/Confirm/{user_id}/{id_reservation}")
    Call<ResponseModel> nursery_accept_refuse_part_reservations(@Path("user_id")String user_id,
                                                                @Path("id_reservation")String id_reservation,
                                                                @Field("accepted_type") String confirm_type,
                                                                @Field("accepted_days_ids[]")List<String>accepted_days_ids_List,
                                                                @Field("refused_days_ids[]")List<String>refused_days_ids_List

    );


    @FormUrlEncoded
    @POST("Api/ConfirmTransformate/{user_id}/{id_reservation}")
    Call<ResponseModel> nursery_accept_refuse_all_payment(@Path("user_id")String user_id,
                                                               @Path("id_reservation")String id_reservation,
                                                               @Field("confirm_type") String confirm_type
    );
    @FormUrlEncoded
    @POST("Api/ConfirmTransformate/{user_id}/{id_reservation}")
    Call<ResponseModel> nursery_accept_refuse_part_payment(@Path("user_id")String user_id,
                                                                @Path("id_reservation")String id_reservation,
                                                                @Field("confirm_type") String confirm_type,
                                                                @Field("accepted_days_ids[]")List<String>accepted_days_ids_List,
                                                                @Field("refused_days_ids[]")List<String>refused_days_ids_List

    );

    @FormUrlEncoded
    @POST("Api/Transformation/{id_reservation}")
    Call<ResponseModel> client_accept_refuse_all_payment(@Path("id_reservation")String id_reservation,
                                                         @Field("transformation_type") String transformation_type

                                                         );


    @Multipart
    @POST("Api/Transformation/{id_reservation}")
    Call<ResponseModel> client_accept_refuse_part_payment(@Path("id_reservation")String id_reservation,
                                                          @Part("transformation_type") RequestBody transformation_type,
                                                          @Part("accepted_days_ids[]")List<RequestBody>accepted_days_ids_List,
                                                          @Part("refused_days_ids[]")List<RequestBody>refused_days_ids_List,
                                                          @Part("transformation_person") RequestBody transformation_person,
                                                          @Part("transformation_phone") RequestBody transformation_phone,
                                                          @Part("transformation_amount") RequestBody transformation_amount,
                                                          @Part MultipartBody.Part transformation_image

    );


    @GET("Api/MyCurrentResevation/{user_id}")
    Call<List<ReservationModel>> getClientCurrentReservations(@Path("user_id") String user_id);

    @GET("Api/MyNewResevation/{user_id}")
    Call<List<ReservationModel>> getClientNewReservations(@Path("user_id") String user_id);

    @GET("Api/CurrentResevation/{user_id}")
    Call<List<ReservationModel>> getNurseryCurrentReservations(@Path("user_id") String user_id);

    @GET("Api/NewResevation/{user_id}")
    Call<List<ReservationModel>> getNurseryNewReservations(@Path("user_id") String user_id);


    @FormUrlEncoded
    @POST("AppUser/UpdatePass/{user_id}")
    Call<UserModel> UpdatePassword(@Path("user_id") String user_id,
                                   @Field("user_old_pass") String user_old_pass,
                                   @Field("user_new_pass") String user_new_pass
    );

    @FormUrlEncoded
    @POST("AppUser/ClientProfile/{user_id}")
    Call<UserModel> UpdateProfileData(@Path("user_id") String user_id,
                                      @Field("user_phone") String user_phone,
                                      @Field("user_full_name") String user_full_name,
                                      @Field("user_name") String user_name

    );

    @FormUrlEncoded
    @POST("AppUser/MyDeleteData/{user_id}")
    Call<UserModel> deleteService(@Path("user_id") String user_id,
                                      @Field("delete_type") String delete_type,
                                      @Field("id_service") String id_service
    );


    @GET("AppUser/Profile/{user_id}")
    Call<List<ServiceModel>> getNursery_UnChoosed_Services(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> UpdateNurseryProfileData(@Path("user_id") String user_id,
                                      @Field("user_full_name")String user_full_name,
                                      @Field("user_name")String user_name,
                                      @Field("user_phone")String user_phone,
                                      @Field("person_responsible_phone")String person_responsible_phone,
                                      @Field("person_responsible_name")String person_responsible_name,
                                      @Field("user_address")String user_address,
                                      @Field("hour_cost")String hour_cost,
                                      @Field("from_hour")String from_hour,
                                      @Field("to_hour")String to_hour

                                      );

    @Multipart
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> UpdateNurseryProfileImage(@Path("user_id") String user_id,
                                       @Part("user_full_name")RequestBody user_full_name,
                                       @Part("user_name")RequestBody user_name,
                                       @Part("user_phone")RequestBody user_phone,
                                       @Part("person_responsible_phone")RequestBody person_responsible_phone,
                                       @Part("person_responsible_name")RequestBody person_responsible_name,
                                       @Part("user_address")RequestBody user_address,
                                       @Part("hour_cost")RequestBody hour_cost,
                                       @Part("from_hour")RequestBody from_hour,
                                       @Part("to_hour")RequestBody to_hour,
                                       @Part MultipartBody.Part image

    );

    @Multipart
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> UpdateNurseryProfileGallery(@Path("user_id") String user_id,
                                         @Part("user_full_name")RequestBody user_full_name,
                                         @Part("user_name")RequestBody user_name,
                                         @Part("user_phone")RequestBody user_phone,
                                         @Part("person_responsible_phone")RequestBody person_responsible_phone,
                                         @Part("person_responsible_name")RequestBody person_responsible_name,
                                         @Part("user_address")RequestBody user_address,
                                         @Part("hour_cost")RequestBody hour_cost,
                                         @Part("from_hour")RequestBody from_hour,
                                         @Part("to_hour")RequestBody to_hour,
                                         @Part List<MultipartBody.Part> gallery

    );

    @FormUrlEncoded
    @POST("AppUser/Profile/{user_id}")
    Call<UserModel> UpdateNurseryProfileServices(@Path("user_id") String user_id,
                                          @Field("user_full_name") String user_full_name,
                                          @Field("user_name") String user_name,
                                          @Field("user_phone") String user_phone,
                                          @Field("person_responsible_phone") String person_responsible_phone,
                                          @Field("person_responsible_name") String person_responsible_name,
                                          @Field("user_address") String user_address,
                                          @Field("hour_cost") String hour_cost,
                                          @Field("from_hour") String from_hour,
                                          @Field("to_hour") String to_hour,
                                          @Field("kindergarten_service[]")  List<String> nursery_service_ids

    );

    @FormUrlEncoded
    @POST("AppUser/MyDeleteData/{user_id}")
    Call<UserModel> deleteGalleryImage(@Path("user_id") String user_id,
                                       @Field("delete_type") String delete_type,
                                       @Field("id_photo") String id_photo
                                       );

}

