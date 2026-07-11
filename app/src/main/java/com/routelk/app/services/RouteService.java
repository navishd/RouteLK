package com.routelk.app.services;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.routelk.app.models.Route;

import java.util.ArrayList;
import java.util.List;



public class RouteService {


    private final FirebaseFirestore db;



    public RouteService(){

        db = FirebaseFirestore.getInstance();

    }






    // ADD ROUTE

    public void addRoute(Route route){



        if(route.getId() == null ||
                route.getId().isEmpty()){


            String id =
                    db.collection("routes")
                            .document()
                            .getId();


            route.setId(id);


        }




        db.collection("routes")
                .document(route.getId())
                .set(route);



    }








    // GET ALL ROUTES

    public void getRoutes(
            OnSuccessListener<List<Route>> listener
    ){


        db.collection("routes")
                .get()

                .addOnSuccessListener(
                        snapshot -> {


                            List<Route> routeList =
                                    new ArrayList<>();



                            for(DocumentSnapshot doc : snapshot){


                                Route route =
                                        doc.toObject(Route.class);



                                if(route != null){


                                    route.setId(
                                            doc.getId()
                                    );


                                    routeList.add(route);


                                }


                            }



                            listener.onSuccess(
                                    routeList
                            );


                        }
                );


    }








    // GET SINGLE ROUTE

    public void getRouteById(
            String routeId,
            OnSuccessListener<Route> listener
    ){



        db.collection("routes")
                .document(routeId)

                .get()

                .addOnSuccessListener(
                        documentSnapshot -> {


                            Route route =
                                    documentSnapshot
                                            .toObject(
                                                    Route.class
                                            );



                            if(route != null){


                                route.setId(
                                        documentSnapshot.getId()
                                );


                            }



                            listener.onSuccess(route);



                        }
                );



    }








    // UPDATE ROUTE

    public void updateRoute(
            Route route
    ){



        db.collection("routes")

                .document(
                        route.getId()
                )

                .set(route);



    }









    // DELETE ROUTE WITH CALLBACK

    public void deleteRoute(
            String routeId,
            OnSuccessListener<Void> listener
    ){



        db.collection("routes")

                .document(routeId)

                .delete()

                .addOnSuccessListener(
                        listener
                );


    }







    // DELETE ROUTE WITHOUT CALLBACK
    // (extra support)

    public void deleteRoute(
            String routeId
    ){


        db.collection("routes")
                .document(routeId)
                .delete();


    }



}