package org.limmen.mystart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.limmen.mystart.exception.StorageException;

public final class SingleUserSerializeLinkStorage extends BaseSerializeStorage {

   private String fileForLinks;

   private List<Link> links = new ArrayList<>();

   public SingleUserSerializeLinkStorage(String userId) throws StorageException {
      fileForLinks = getFileName(userId + "-links.ser");

      try {
         load();
      }
      catch (StorageException ex) {
         save();
      }
   }

   public void store(Link link) throws StorageException {
      Optional<Link> current = this.links.stream().filter(l -> {
         return l.equals(link);
      }).findFirst();     

      if (!current.isPresent()) {
         this.links.add(link);
      } else {
         LOGGER.info("Updating link {} as we allready have it.", link.getUrl());

         current.get().setLabels(link.getLabels());
         current.get().setDescription(link.getDescription());
         current.get().setTitle(link.getTitle());
         current.get().setSource(link.getSource());
         current.get().setLastVisit(link.getLastVisit());
         current.get().setPrivateNetwork(link.isPrivateNetwork());
      }

      save();
   }

   public void storeCollection(Collection<Link> links) throws StorageException {
      links.forEach(l -> {
         if (!this.links.contains(l)) {
            if (l.getUrl() != null) {
               this.links.add(l);
            } else {
               LOGGER.info("Not adding link {} with title {} as it is empty.", l.getUrl(), l.getTitle());
            }
         } else {
            LOGGER.info("Not adding link {} as we allready have it.", l.getUrl());
         }
      });

      save();
   }

   public Collection<Link> getAll() throws StorageException {
      return this.links.stream().sorted((c1, c2) -> {
         return c1.getUrl().compareTo(c2.getUrl());
      }).collect(Collectors.toList());
   }

   public Collection<Link> getAllByLabel(String tagName) {
      return links.stream().filter(p -> p.getLabels().contains(tagName)).collect(Collectors.toList());
   }

   public Collection<String> getAllLabels() throws StorageException {
      Set<String> labels = new HashSet<>();
      
      this.links.stream().forEach((l -> {
         labels.addAll(l.getLabels());
      }));
      
      
      return labels.stream().sorted((c1, c2) -> {
         return c1.compareToIgnoreCase(c2);
      }).collect(Collectors.toList());
   }

   private void save() throws StorageException {
      save(links, fileForLinks);
   }

   @SuppressWarnings("unchecked")
   private void load() throws StorageException {
      this.links = load(fileForLinks);
   }

   public void remove(Long id) throws StorageException {
      Optional<Link> link = this.links.stream().filter(p -> {
         return id != null && id.equals(p.getId());
      }).findFirst();

      if (link.isPresent()) {
         this.links.remove(link.get());
         save();
      }
   }

   public Link get(Long id) throws StorageException {
      Optional<Link> link = this.links.stream().filter(p -> {
         return id.equals(p.getId());
      }).findFirst();

      if (link.isPresent()) {
         return link.get();
      } else {
         return null;
      }
   }
}
