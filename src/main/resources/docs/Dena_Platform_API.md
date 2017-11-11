# Working With Object #

----------

## Storing Object ##



    {
      "app_id": "dena_qa"
      "object_id": "asdad6876876",
      "type": "question",
      "related_objects": [
        {
          "id": "123123",
          "type": "para"
        }
      ],
      "object_values": [
        {
          "field1": "javad",
          "field2": "developer"
        }
      ]
    }
    

----------

## Delete Object ##

**Delete One Object**

Method: DELETE

URL: /v1/<application-id>/<type-name>/<object-id>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Bulk Objects**

Method: DELETE

URL: /v1/<application-id>/<type-name>/<object-id-1=id1>,<object-id=2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
Delete Relation (With All Child)
Method:
  DELETE

URL:
  /v1/<application-id>/<type-name-1>/<object-id-1>/relation/<type-name-2>

Request Body:
  None

  Return Value:
  {
   "deletion_time" : timestamp in milliseconds,
   "deleted_item_count" : number of deleted object(s)
  }

======================================================================================================================================================
Delete Relation With Specified Child Object
Method:
  DELETE

URL:
  /v1/<application-id>/<type-name-1>/<object-id-1>/relation/<type-name-2>/<object-id-2>

Request Body:
  None

  Return Value:
  {
   "deletion_time" : timestamp in milliseconds,
   "deleted_item_count" : number of deleted object(s)
  }