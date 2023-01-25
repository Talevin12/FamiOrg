package com.example.famiorg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.Family;
import com.example.famiorg.logic.GroceryProduct;
import com.example.famiorg.logic.ImagePost;
import com.example.famiorg.logic.MemberInvitation;
import com.example.famiorg.logic.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataManager {

    private Callback_DataManager callback_addOrUpdateFamilyMember;
    private Callback_DataManager callback_removeFamilyMember;
    private Callback_DataManager callback_setUser;
    private Callback_DataManager callback_setFamilyName;
    private Callback_DataManager callback_addGroceryProduct;
    private Callback_DataManager callback_updateGroceryProduct;
    private Callback_DataManager callback_moveGroceryProduct;
    private Callback_DataManager callback_removeGroceryProduct;
    private Callback_DataManager callback_getImagePosts;
    private Callback_DataManager callback_getFamilyMembers;
    private Callback_DataManager callback_createFamily;
    private Callback_DataManager callback_setUserFamily;
    private Callback_DataManager callback_createInvitation;
    private Callback_DataManager callback_getUserInvitations;

    public void setCallBack_addOrUpdateFamilyMemberProtocol(Callback_DataManager callback_addOrUpdateFamilyMember) {
        this.callback_addOrUpdateFamilyMember = callback_addOrUpdateFamilyMember;
    }

    public void setCallBack_removeFamilyMemberProtocol(Callback_DataManager callback_removeFamilyMember) {
        this.callback_removeFamilyMember = callback_removeFamilyMember;
    }

    public void setCallBack_setUserProtocol(Callback_DataManager callback_setUser) {
        this.callback_setUser = callback_setUser;
    }

    public void setCallBack_setFamilyName(Callback_DataManager callback_setFamilyName) {
        this.callback_setFamilyName = callback_setFamilyName;
    }

    public void setCallback_addGroceryProduct(Callback_DataManager callback_addGroceryProduct) {
        this.callback_addGroceryProduct = callback_addGroceryProduct;
    }

    public void setCallback_updateGroceryProduct(Callback_DataManager callback_updateGroceryProduct) {
        this.callback_updateGroceryProduct = callback_updateGroceryProduct;
    }

    public void setCallback_moveGroceryProduct(Callback_DataManager callback_moveGroceryProduct) {
        this.callback_moveGroceryProduct = callback_moveGroceryProduct;
    }

    public void setCallback_removeGroceryProduct(Callback_DataManager callback_removeGroceryProduct) {
        this.callback_removeGroceryProduct = callback_removeGroceryProduct;
    }

    public void setCallback_getFamilyMembers(Callback_DataManager callback_getFamilyMembers) {
        this.callback_getFamilyMembers = callback_getFamilyMembers;
    }

    public void setCallback_getImagePosts(Callback_DataManager callback_getImagePosts) {
        this.callback_getImagePosts = callback_getImagePosts;
    }

    public void setCallback_createFamily(Callback_DataManager callback_createFamily) {
        this.callback_createFamily = callback_createFamily;
    }

    public void setCallback_setUserFamily(Callback_DataManager callback_setUserFamily) {
        this.callback_setUserFamily = callback_setUserFamily;
    }

    public void setCallback_createInvitation(Callback_DataManager callback_createInvitation) {
        this.callback_createInvitation = callback_createInvitation;
    }

    public void setCallback_getUserInvitations(Callback_DataManager callback_getUserInvitations) {
        this.callback_getUserInvitations = callback_getUserInvitations;
    }

    public void getGroceries(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_addGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_updateGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback_removeGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                callback_moveGroceryProduct.getObject(new int[] {snapshot.getValue(GroceryProduct.class).getPosition(), Integer.valueOf(previousChildName)});
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addGrocery(String famId, GroceryProduct groceryProduct) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.child(String.valueOf(groceryProduct.getId())).setValue(groceryProduct);
    }

    public void removeGrocery(String famId, GroceryProduct groceryProduct) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.child(groceryProduct.getId()).removeValue();
    }

    public void updateGroceryNameAndQuantity(String famId, String groceryId, String name, float quantity) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);

        refProduct.child("name").setValue(name);
        refProduct.child("quantity").setValue(quantity);
    }

    public void updateGroceryIsDone(String famId, String groceryId, boolean isDone) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);

        refProduct.child("isDone").setValue(isDone);
    }

//    public void moveGrocery(String famId, String groceryId, int from, int to) {
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);
//
////        refProduct.
//    }

    public void getFamilyMembersRT(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refFamilY = db.getReference("Families").child(famId).child("familyMembersIDs");

        refFamilY.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                refUsers.child(snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback_addOrUpdateFamilyMember.getObject(snapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                refUsers.child(snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback_removeFamilyMember.getObject(snapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getFamilyMembers(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refFamilY = db.getReference("Families").child(famId).child("familyMembersIDs");

        refFamilY.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                ArrayList<User> famMembers = new ArrayList<>();

                for (DataSnapshot child : snapshot1.getChildren()) {
                    refUsers.child(child.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            famMembers.add(snapshot2.getValue(User.class));

                            if (snapshot1.getChildrenCount() == famMembers.size()) {
                                callback_getFamilyMembers.getObject(famMembers);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addImage(String famId, ImagePost newImagePost) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refImagePosts = db.getReference("Families").child(famId).child("imagePosts");

        refImagePosts.child(String.valueOf(newImagePost.getId())).setValue(newImagePost);
    }

    public void getImages(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refImagePosts = db.getReference("Families").child(famId).child("imagePosts");

        refImagePosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ImagePost> imagePosts = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    imagePosts.add(child.getValue(ImagePost.class));
                }

                callback_getImagePosts.getObject(imagePosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getUser(String userId, boolean isRT) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUser = db.getReference("Users").child(userId);

        if (isRT) {
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callback_setUser.getObject(snapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callback_setUser.getObject(snapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void getFamilyName(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamilyName = db.getReference("Families").child(famId).child("familyName");

        refFamilyName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback_setFamilyName.getObject(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateUserName(String userId, String newName) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserName = db.getReference("Users").child(userId).child("name");

        refUserName.setValue(newName);
    }

    public void updateUserIcon(String userId, Integer newIcon) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserName = db.getReference("Users").child(userId).child("icon");

        refUserName.setValue(newIcon);
    }

    public void createFamily(Family newFam) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refFamily = db.getReference("Families").child(newFam.getFamilyId());

        refFamily.child("familyMembersIDs").setValue(newFam.getFamilyMembersIDs());
        refFamily.child("familyName").setValue(newFam.getFamilyName());

        callback_createFamily.getObject(newFam.getFamilyId());
    }

    public void updateUserFamilyAndAddToFamily(String userId, String famId, boolean isCreator) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserFamily = db.getReference("Users").child(userId).child("familyId");

        refUserFamily.setValue(famId);

        if (!isCreator) {
            DatabaseReference refFamilyMembers = db.getReference("Families").child(famId).child("familyMembersIDs");
            refFamilyMembers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<>() {
                    };
                    ArrayList<String> members = snapshot.getValue(t);
                    members.add(userId);
                    refFamilyMembers.setValue(members);


                    callback_setUserFamily.getObject(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            callback_setUserFamily.getObject(null);
        }
    }

    public void createInvitation(MemberInvitation memberInvitation) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsers = db.getReference("Users");
        DatabaseReference refInvitations = db.getReference("Invitations");

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue(User.class).getEmail().equalsIgnoreCase(memberInvitation.getUserRecvEmail())) {
                        refInvitations.child(memberInvitation.getInvitationId()).setValue(memberInvitation);
                        flag = true;
                        break;
                    }
                }

                callback_createInvitation.getObject(flag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getUserInvitations(String email) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refInvitations = db.getReference("Invitations");

        refInvitations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MemberInvitation> invitations = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    MemberInvitation invitation = child.getValue(MemberInvitation.class);
                    if (invitation.getUserRecvEmail().equalsIgnoreCase(email)) {
                        invitations.add(invitation);
                    }
                }

                callback_getUserInvitations.getObject(invitations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeInvitation(String invitationId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refInvitation = db.getReference("Invitations").child(invitationId);

        refInvitation.removeValue();
    }

    public void removeMemberFromFamily(String userId, String familyId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUserFamily = db.getReference("Users").child(userId).child("familyId");
        DatabaseReference refFamilyMembers = db.getReference("Families").child(familyId).child("familyMembersIDs");

        refUserFamily.removeValue();

        refFamilyMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<>() {
                };
                ArrayList<String> members = snapshot.getValue(t);

                members.remove(userId);
                refFamilyMembers.removeValue();
                refFamilyMembers.setValue(members);

                callback_removeFamilyMember.getObject(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
