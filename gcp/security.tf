resource "google_compute_firewall" "app" {
  name    = "selenium-demo-firewall"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["22", "80", "8080"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["selenium-demo"]
}
