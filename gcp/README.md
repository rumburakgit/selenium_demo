## komendy dla gcp
gcloud --version
gcloud auth login
gcloud auth application-default login
gcloud config set project project-267303b5-6e26-4ae7-81c

## komendy dla terraform
terraform --version

cd gcp
terraform init
terraform plan
terraform apply

terraform state list
 
śledzenie postępu: 
ssh -i ~/.ssh/kaz_to_git ubuntu@<public_ip>
tail -f /var/log/cloud-init-output.log

np:
ssh -i ~/.ssh/kaz_to_git ubuntu@34.116.236.32
tail -f /var/log/cloud-init-output.log

terraform destroy
