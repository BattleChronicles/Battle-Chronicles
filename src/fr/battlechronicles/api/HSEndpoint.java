package fr.battlechronicles.api;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "hSEndpoint",
        namespace = @ApiNamespace(
                ownerDomain = "battlechronicles",
                ownerName = "battlechronicles",
                packagePath="api"
        )
)
public class HSEndpoint {
    // from http://www.programcreek.com/java-api-examples/index.php?api=com.google.appengine.api.datastore.Cursor

    @SuppressWarnings({ "unchecked", "unused" })
    @ApiMethod(name = "highscores")
    public CollectionResponse<Score> highscores(
                    @Nullable @Named("cursor") String cursorString,
                    @Nullable @Named("limit") Integer limit) {

            PersistenceManager mgr = null;
            Cursor cursor = null;
            List<Score> execute = null;

            try {
                    mgr = getPersistenceManager();
                    Query query = mgr.newQuery(Score.class);
                    if (cursorString != null && cursorString != "") {
                            cursor = Cursor.fromWebSafeString(cursorString);
                            HashMap<String, Object> extensionMap = new HashMap<String, Object>();
                            extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
                            query.setExtensions(extensionMap);
                    }

                    if (limit != null) {
                        query.setOrdering("score desc");
                        query.setRange(0, 10);
                    }

                    execute = (List<Score>) query.execute();
                    cursor = JDOCursorHelper.getCursor(execute);
                    if (cursor != null)
                            cursorString = cursor.toWebSafeString();

                    // Tight loop for fetching all entities from datastore and accomodate
                    // for lazy fetch.
                    for (Score obj : execute)
                            ;
            } finally {
                    mgr.close();
            }

            return CollectionResponse.<Score> builder().setItems(execute)
                            .setNextPageToken(cursorString).build();
    }



    private static PersistenceManager getPersistenceManager() {
        return PMF.get().getPersistenceManager();
    }
}